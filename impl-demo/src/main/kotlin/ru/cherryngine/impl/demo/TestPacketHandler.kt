package ru.cherryngine.impl.demo

import io.github.dockyardmc.PacketHandler
import io.github.dockyardmc.cherry.math.Vec3D
import io.github.dockyardmc.cherry.math.View
import io.github.dockyardmc.protocol.packets.ProtocolState
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.packets.common.ClientboundPongResponsePacket
import io.github.dockyardmc.protocol.packets.common.ClientboundUpdateTagsPacket
import io.github.dockyardmc.protocol.packets.common.ServerboundPingRequestPacket
import io.github.dockyardmc.protocol.packets.configurations.ClientboundFinishConfigurationPacket
import io.github.dockyardmc.protocol.packets.configurations.ClientboundRegistryDataPacket
import io.github.dockyardmc.protocol.packets.configurations.ServerboundFinishConfigurationPacket
import io.github.dockyardmc.protocol.packets.handshake.ServerboundIntentionPacket
import io.github.dockyardmc.protocol.packets.login.ClientboundLoginFinishedPacket
import io.github.dockyardmc.protocol.packets.login.ServerboundHelloPacket
import io.github.dockyardmc.protocol.packets.login.ServerboundLoginAcknowledgedPacket
import io.github.dockyardmc.protocol.packets.play.clientbound.ClientboundGameEventPacket
import io.github.dockyardmc.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import io.github.dockyardmc.protocol.packets.play.clientbound.ClientboundLoginPacket
import io.github.dockyardmc.protocol.packets.play.clientbound.ClientboundSetChunkCacheCenterPacket
import io.github.dockyardmc.protocol.packets.play.serverbound.*
import io.github.dockyardmc.protocol.packets.status.ClientboundStatusResponsePacket
import io.github.dockyardmc.protocol.packets.status.ServerboundStatusRequestPacket
import io.github.dockyardmc.protocol.types.ChunkPos
import io.github.dockyardmc.protocol.types.GameMode
import io.github.dockyardmc.protocol.types.GameProfile
import io.github.dockyardmc.protocol.types.MovePlayerFlags
import io.github.dockyardmc.registry.DimensionTypes
import io.github.dockyardmc.registry.RegistryManager
import io.github.dockyardmc.registry.registries.tags.*
import io.github.dockyardmc.server.Connection
import io.github.dockyardmc.world.Light
import io.github.dockyardmc.world.chunk.ChunkData
import io.github.dockyardmc.world.test.Chunk
import jakarta.inject.Singleton

@Singleton
class TestPacketHandler(
    val testWorldShit: TestWorldShit,
    val playerManager: PlayerManager,
    val mainScene: MainScene,
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