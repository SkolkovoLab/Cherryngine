package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerGameComponent
import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.impl.demo.view.PlayerViewSystem
import ru.cherryngine.impl.demo.world.TestWorldShit
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.PacketHandler
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
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundGameEventPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLoginPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerPosPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerPosRotPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerRotPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerStatusOnlyPacket
import ru.cherryngine.lib.minecraft.protocol.packets.status.ClientboundStatusResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.status.ServerboundStatusRequestPacket
import ru.cherryngine.lib.minecraft.protocol.types.GameMode
import ru.cherryngine.lib.minecraft.protocol.types.GameProfile
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.registry.DimensionTypes
import ru.cherryngine.lib.minecraft.registry.RegistryManager
import ru.cherryngine.lib.minecraft.registry.registries.tags.*
import ru.cherryngine.lib.minecraft.server.Connection

class PlayerInitGameSystem(
    val gameScene: GameScene,
    val testWorldShit: TestWorldShit,
) : GameSystem, PacketHandler {
    val players = mutableMapOf<Connection, Player>()
    var queues = hashMapOf<Connection, MutableList<ServerboundPacket>>()

    override fun tick(tickIndex: Long, tickStartMs: Long) {
        val queues = this.queues
        this.queues = hashMapOf()
        gameScene.objectsWithComponent(PlayerGameComponent::class).forEach { gameObject ->
            val playerGameComponent = gameObject[PlayerGameComponent::class]!!
            val packets = queues[playerGameComponent.player.connection] ?: listOf()
            gameObject[PlayerGameComponent::class] = playerGameComponent.copy(packets = packets)
        }
    }

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

                val player = Player(connection)
                players[connection] = player
                gameScene.createGameObject()[PlayerGameComponent::class] = PlayerGameComponent(
                    player,
                    listOf(),
                    PlayerViewSystem(player),
                )

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

                gameScene.objectsWithComponent(PlayerGameComponent::class).forEach { gameObject ->
                    val playerGameComponent = gameObject[PlayerGameComponent::class]!!
                    if (playerGameComponent.player.connection == connection) {
                        gameObject[PlayerGameComponent::class] =
                            playerGameComponent.copy(world = testWorldShit.normalWorld)
                    }
                }
            }

            is ServerboundMovePlayerPosPacket -> onMove(connection, packet.pos, null, packet.flags)
            is ServerboundMovePlayerPosRotPacket -> onMove(connection, packet.pos, packet.yawPitch, packet.flags)
            is ServerboundMovePlayerRotPacket -> onMove(connection, null, packet.yawPitch, packet.flags)
            is ServerboundMovePlayerStatusOnlyPacket -> onMove(connection, null, null, packet.flags)

            else -> {
                val queue = queues.computeIfAbsent(connection) { arrayListOf() }
                queue.add(packet)
            }
        }
    }

    fun onMove(
        connection: Connection,
        pos: Vec3D?,
        yawPitch: YawPitch?,
        flags: MovePlayerFlags,
    ) {
        val player = players[connection] ?: return
        if (pos != null) player.lastPosition = pos
        if (yawPitch != null) player.lastYawPitch = yawPitch
    }
}