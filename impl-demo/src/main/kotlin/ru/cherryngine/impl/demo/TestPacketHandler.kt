package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.impl.demo.player.PlayerManager
import ru.cherryngine.impl.demo.world.PlayerChunkView
import ru.cherryngine.impl.demo.world.TestWorldShit
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import ru.cherryngine.lib.minecraft.PacketHandler
import ru.cherryngine.lib.minecraft.entity.MetaContainer
import ru.cherryngine.lib.minecraft.entity.MetadataDef
import ru.cherryngine.lib.minecraft.entity.flags.EntityMetaFlags
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundPongResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundUpdateTagsPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ServerboundPingRequestPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ServerboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.handshake.ServerboundIntentionPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ClientboundLoginFinishedPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundHelloPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundLoginAcknowledgedPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundAddEntityPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundGameEventPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLoginPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundSetEntityDataPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.*
import ru.cherryngine.lib.minecraft.protocol.packets.status.ClientboundStatusResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.status.ServerboundStatusRequestPacket
import ru.cherryngine.lib.minecraft.protocol.types.GameMode
import ru.cherryngine.lib.minecraft.protocol.types.GameProfile
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.registry.CatVariants
import ru.cherryngine.lib.minecraft.registry.DimensionTypes
import ru.cherryngine.lib.minecraft.registry.EntityTypes
import ru.cherryngine.lib.minecraft.registry.RegistryManager
import ru.cherryngine.lib.minecraft.registry.registries.tags.*
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*

@Singleton
class TestPacketHandler(
    val testWorldShit: TestWorldShit,
    val playerManager: PlayerManager,
) : PacketHandler {
    override fun onPacket(connection: Connection, packet: ServerboundPacket) {
        when (packet) {
            is ServerboundIntentionPacket -> {
                connection.state = when (packet.intent) {
                    ServerboundIntentionPacket.Intent.STATUS -> ProtocolState.STATUS
                    ServerboundIntentionPacket.Intent.LOGIN -> ProtocolState.LOGIN
                    ServerboundIntentionPacket.Intent.TRANSFER -> ProtocolState.LOGIN
                }
            }

            is ServerboundPingRequestPacket -> {
                connection.sendPacket(ClientboundPongResponsePacket(packet.time))
            }

            is ServerboundStatusRequestPacket -> {
                connection.sendPacket(
                    ClientboundStatusResponsePacket(
                        """
                    {
                        "version": {
                            "name": "1.21.8",
                            "protocol": 772
                        },
                        "players": {
                            "max": 0,
                            "online": 0
                        },
                        "description": {
                            "text": "Hello, world!"
                        }
                    }
                    """.trimIndent()
                    )
                )
            }

            is ServerboundHelloPacket -> {
                val gameProfile = GameProfile(packet.uuid, packet.name, mutableListOf())
                connection.sendPacket(ClientboundLoginFinishedPacket(gameProfile))
            }

            is ServerboundLoginAcknowledgedPacket -> {
                connection.state = ProtocolState.CONFIGURATION

                playerManager.map[connection] = Player(connection)

                val cachedTagPacket = ClientboundUpdateTagsPacket(
                    listOf(
                        BiomeTagRegistry,
                        ItemTagRegistry,
                        BlockTagRegistry,
                        FluidTagRegistry,
                        EntityTypeTagRegistry
                    )
                )
                connection.sendPacket(cachedTagPacket)

                RegistryManager.dynamicRegistries.values.forEach { registry ->
                    connection.sendPacket(ClientboundRegistryDataPacket(registry))
                }
                connection.sendPacket(ClientboundFinishConfigurationPacket())
            }

            is ServerboundFinishConfigurationPacket -> {
                connection.state = ProtocolState.PLAY
                connection.sendPacket(
                    ClientboundLoginPacket(
                        0,
                        false,
                        listOf(),
                        20,
                        8,
                        8,
                        false,
                        true,
                        false,
                        DimensionTypes.OVERWORLD,
                        "world",
                        0L,
                        GameMode.CREATIVE,
                        GameMode.CREATIVE,
                        false,
                        false,
                        null,
                        0,
                        32,
                        false
                    )
                )

                connection.sendPacket(
                    ClientboundGameEventPacket(
                        ClientboundGameEventPacket.GameEvent.START_WAITING_FOR_CHUNKS,
                        0f
                    )
                )

                val player = playerManager.map[connection] ?: return

                val playerChunkView = PlayerChunkView(player, testWorldShit.world)
                player.playerChunkView = playerChunkView

                playerChunkView.init()
            }

            is ServerboundPlayerLoadedPacket -> {
                println("player loaded!")
            }

            is ServerboundMovePlayerPosPacket -> onMove(connection, packet.pos, null, packet.flags)
            is ServerboundMovePlayerPosRotPacket -> onMove(connection, packet.pos, packet.view, packet.flags)
            is ServerboundMovePlayerRotPacket -> onMove(connection, null, packet.view, packet.flags)
            is ServerboundMovePlayerStatusOnlyPacket -> onMove(connection, null, null, packet.flags)

            is ServerboundClientTickEndPacket -> {
                playerManager.map[connection]?.tick()
            }

            is ServerboundChatCommandPacket -> {
                val player = playerManager.map[connection] ?: return
                when (packet.command) {
                    "test1" -> {
                        connection.sendPacket(
                            ClientboundAddEntityPacket(
                                228, UUID.randomUUID(),
                                EntityTypes.CAT,
                                player.clientPosition,
                                View.ZERO, 0f,
                                0,
                                Vec3D.ZERO
                            )
                        )

                        val metaContainer = MetaContainer()
                        metaContainer[MetadataDef.Entity.ENTITY_FLAGS] = EntityMetaFlags(hasGlowingEffects = true)
                        metaContainer[MetadataDef.Entity.HAS_NO_GRAVITY] = true
                        metaContainer[MetadataDef.Cat.VARIANT] = CatVariants.RED

                        connection.sendPacket(
                            ClientboundSetEntityDataPacket(
                                228,
                                metaContainer.entries
                            )
                        )
                    }
                }
            }
        }
    }

    fun onMove(
        connection: Connection,
        pos: Vec3D?,
        view: View?,
        flags: MovePlayerFlags,
    ) {
        val player = playerManager.map[connection] ?: return
        if (pos != null) player.lastPosition = pos
    }
}