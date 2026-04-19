package ru.cherryngine.engine.bedrock

import io.netty.buffer.Unpooled
import org.cloudburstmc.math.vector.Vector2f
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.math.vector.Vector3i
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtUtils
import org.cloudburstmc.protocol.bedrock.BedrockServerSession
import org.cloudburstmc.protocol.bedrock.codec.v944.Bedrock_v944
import org.cloudburstmc.protocol.bedrock.data.*
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag
import org.cloudburstmc.protocol.bedrock.packet.*
import org.cloudburstmc.protocol.bedrock.util.EncryptionUtils
import org.cloudburstmc.protocol.common.PacketSignal
import org.cloudburstmc.protocol.common.util.OptionalBoolean
import ru.cherryngine.engine.core.player.InstanceRouter
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerRouter
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.io.ByteArrayOutputStream
import java.util.*

class BedrockSessionHandler(
    private val session: BedrockServerSession,
    private val playerManager: PlayerManager,
    private val instanceRouter: InstanceRouter,
    private val playerRouter: PlayerRouter,
    private val onReady: (BedrockPlayer) -> Unit,
) : BedrockPacketHandler {

    private var player: BedrockPlayer? = null
    private val log = org.slf4j.LoggerFactory.getLogger(BedrockSessionHandler::class.java)

    override fun handlePacket(packet: BedrockPacket): PacketSignal {
        if (packet !is PlayerAuthInputPacket) {
            log.debug("<<< {}", packet.javaClass.simpleName)
        }
        return super.handlePacket(packet)
    }

    companion object {
        private val EMPTY_LEVEL_CHUNK_DATA: ByteArray = run {
            val out = ByteArrayOutputStream()
            out.write(ByteArray(258))
            NbtUtils.createNetworkWriter(out).use { it.writeTag(NbtMap.EMPTY) }
            out.toByteArray()
        }
    }

    // ========== Handshake ==========

    override fun handle(packet: RequestNetworkSettingsPacket): PacketSignal {
        session.codec = Bedrock_v944.CODEC
        val settings = NetworkSettingsPacket()
        settings.compressionAlgorithm = PacketCompressionAlgorithm.ZLIB
        settings.compressionThreshold = 0
        session.sendPacketImmediately(settings)
        session.setCompression(PacketCompressionAlgorithm.ZLIB)
        return PacketSignal.HANDLED
    }

    override fun handle(packet: LoginPacket): PacketSignal {
        val chainResult = EncryptionUtils.validatePayload(packet.authPayload)
        val claims = chainResult.identityClaims()
        val identityPublicKey = claims.parsedIdentityPublicKey()

        val username = claims.extraData.displayName ?: extractUsername(packet.clientJwt) ?: "Player"
        val uuid = claims.extraData.identity ?: UUID.nameUUIDFromBytes(username.toByteArray())
        player = BedrockPlayer(session, uuid, username)

        val serverKeyPair = EncryptionUtils.createKeyPair()
        val token = EncryptionUtils.generateRandomToken()
        val secretKey = EncryptionUtils.getSecretKey(serverKeyPair.private, identityPublicKey, token)
        val jwt = EncryptionUtils.createHandshakeJwt(serverKeyPair, token)

        val handshake = ServerToClientHandshakePacket()
        handshake.jwt = jwt
        session.sendPacketImmediately(handshake)
        session.enableEncryption(secretKey)

        return PacketSignal.HANDLED
    }

    override fun handle(packet: ClientToServerHandshakePacket): PacketSignal {
        session.sendPacket(PlayStatusPacket().apply { status = PlayStatusPacket.Status.LOGIN_SUCCESS })
        val resourcePacksInfo = ResourcePacksInfoPacket()
        resourcePacksInfo.isForcedToAccept = false
        resourcePacksInfo.worldTemplateId = UUID.randomUUID()
        resourcePacksInfo.worldTemplateVersion = "*"
        session.sendPacket(resourcePacksInfo)
        return PacketSignal.HANDLED
    }

    override fun handle(packet: ResourcePackClientResponsePacket): PacketSignal {
        when (packet.status) {
            ResourcePackClientResponsePacket.Status.HAVE_ALL_PACKS -> {
                val stack = ResourcePackStackPacket()
                stack.isForcedToAccept = false
                stack.gameVersion = "*"
                session.sendPacket(stack)
            }
            ResourcePackClientResponsePacket.Status.COMPLETED -> sendStartGame()
            else -> {}
        }
        return PacketSignal.HANDLED
    }

    // ========== Game ==========

    override fun handle(packet: RequestChunkRadiusPacket): PacketSignal {
        val chunkRadiusUpdated = ChunkRadiusUpdatedPacket()
        chunkRadiusUpdated.radius = packet.radius
        session.sendPacketImmediately(chunkRadiusUpdated)
        session.sendPacket(PlayStatusPacket().apply { status = PlayStatusPacket.Status.PLAYER_SPAWN })
        return PacketSignal.HANDLED
    }

    override fun handle(packet: SetLocalPlayerAsInitializedPacket): PacketSignal {
        val p = player ?: return PacketSignal.HANDLED
        playerManager.register(p)
        instanceRouter.routePlayer(p.uuid, playerRouter.getInitialInstance(p))
        onReady(p)
        return PacketSignal.HANDLED
    }

    override fun handle(packet: CommandRequestPacket): PacketSignal {
        val p = player ?: return PacketSignal.HANDLED
        val command = packet.command.removePrefix("/")
        p.pendingCommands.offer(command)
        return PacketSignal.HANDLED
    }

    override fun handle(packet: PlayerAuthInputPacket): PacketSignal {
        val p = player ?: return PacketSignal.HANDLED
        p.clientPosition = Vec3D(
            packet.position.x.toDouble(),
            packet.position.y.toDouble() - 1.62,
            packet.position.z.toDouble()
        )
        p.clientYawPitch = YawPitch(packet.rotation.y, packet.rotation.x)
        return PacketSignal.HANDLED
    }

    override fun onDisconnect(reason: CharSequence) {
        val p = player ?: return
        instanceRouter.removePlayer(p)
        playerManager.unregister(p.uuid)
    }

    // ========== Spawn sequence ==========

    private fun sendStartGame() {
        val p = player ?: return

        val startGame = buildStartGamePacket(p)
        session.sendPacket(startGame)
        session.sendPacket(ItemComponentPacket())
        session.sendPacket(CreativeContentPacket())

        // Empty chunks 6x6 grid for initial spawn
        for (x in -3 until 3) {
            for (z in -3 until 3) {
                val chunk = LevelChunkPacket()
                chunk.chunkX = x
                chunk.chunkZ = z
                chunk.subChunksLength = 0
                chunk.data = Unpooled.wrappedBuffer(EMPTY_LEVEL_CHUNK_DATA)
                session.sendPacket(chunk)
            }
        }

        // Entity flags (HAS_GRAVITY required for physics)
        val entityData = SetEntityDataPacket()
        entityData.runtimeEntityId = p.runtimeEntityId
        entityData.metadata.getOrCreateFlags().apply {
            put(EntityFlag.HAS_GRAVITY, true)
            put(EntityFlag.HAS_COLLISION, true)
        }
        session.sendPacket(entityData)

        // Movement speed
        val attributes = UpdateAttributesPacket()
        attributes.runtimeEntityId = p.runtimeEntityId
        attributes.attributes = listOf(
            AttributeData("minecraft:movement", 0f, 0.24f, 0.1f, 0.1f)
        )
        session.sendPacket(attributes)

        session.sendPacket(PlayStatusPacket().apply { status = PlayStatusPacket.Status.PLAYER_SPAWN })
    }

    private fun buildStartGamePacket(p: BedrockPlayer): StartGamePacket {
        val startGame = StartGamePacket()
        startGame.uniqueEntityId = p.runtimeEntityId
        startGame.runtimeEntityId = p.runtimeEntityId
        startGame.playerGameType = GameType.CREATIVE
        startGame.playerPosition = Vector3f.from(0f, 64f, 0f)
        startGame.rotation = Vector2f.from(1f, 1f)
        startGame.serverId = ""
        startGame.worldId = ""
        startGame.scenarioId = ""
        startGame.ownerId = ""
        startGame.seed = -1L
        startGame.dimensionId = 0
        startGame.generatorId = 1
        startGame.levelGameType = GameType.CREATIVE
        startGame.difficulty = 1
        startGame.defaultSpawn = Vector3i.ZERO
        startGame.isAchievementsDisabled = true
        startGame.currentTick = -1
        startGame.eduEditionOffers = 0
        startGame.isEduFeaturesEnabled = false
        startGame.rainLevel = 0f
        startGame.lightningLevel = 0f
        startGame.isMultiplayerGame = true
        startGame.isBroadcastingToLan = true
        startGame.platformBroadcastMode = GamePublishSetting.PUBLIC
        startGame.xblBroadcastMode = GamePublishSetting.PUBLIC
        startGame.isCommandsEnabled = true
        startGame.isTexturePacksRequired = false
        startGame.isBonusChestEnabled = false
        startGame.isStartingWithMap = false
        startGame.isTrustingPlayers = true
        startGame.defaultPlayerPermission = PlayerPermission.MEMBER
        startGame.serverChunkTickRange = 4
        startGame.isBehaviorPackLocked = false
        startGame.isResourcePackLocked = false
        startGame.isFromLockedWorldTemplate = false
        startGame.isUsingMsaGamertagsOnly = false
        startGame.isFromWorldTemplate = false
        startGame.isWorldTemplateOptionLocked = false
        startGame.spawnBiomeType = SpawnBiomeType.DEFAULT
        startGame.customBiomeName = ""
        startGame.educationProductionId = ""
        startGame.forceExperimentalGameplay = OptionalBoolean.empty()
        startGame.authoritativeMovementMode = AuthoritativeMovementMode.CLIENT
        startGame.rewindHistorySize = 0
        startGame.isServerAuthoritativeBlockBreaking = false
        startGame.vanillaVersion = "*"
        startGame.isInventoriesServerAuthoritative = true
        startGame.serverEngine = ""
        startGame.levelId = "world"
        startGame.setLevelName("world")
        startGame.premiumWorldTemplateId = "00000000-0000-0000-0000-000000000000"
        startGame.currentTick = 0
        startGame.enchantmentSeed = 0
        startGame.multiplayerCorrelationId = ""
        startGame.playerPropertyData = NbtMap.EMPTY
        startGame.worldTemplateId = UUID.randomUUID()
        startGame.chatRestrictionLevel = ChatRestrictionLevel.NONE
        startGame.isDisablingPlayerInteractions = false
        startGame.isDisablingPersonas = false
        startGame.isDisablingCustomSkins = false
        startGame.isBlockNetworkIdsHashed = false
        startGame.isCreatedInEditor = false
        startGame.isExportedFromEditor = false
        startGame.isEmoteChatMuted = false
        startGame.isHardcore = false
        return startGame
    }

    private fun extractUsername(clientJwt: String): String? {
        return try {
            val parts = clientJwt.split(".")
            if (parts.size < 2) return null
            val payload = String(Base64.getUrlDecoder().decode(parts[1]))
            Regex("\"DisplayName\"\\s*:\\s*\"([^\"]+)\"").find(payload)?.groupValues?.get(1)
        } catch (e: Exception) {
            null
        }
    }
}
