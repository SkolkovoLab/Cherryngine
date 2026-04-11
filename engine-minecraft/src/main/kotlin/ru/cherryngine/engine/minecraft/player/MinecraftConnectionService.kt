package ru.cherryngine.engine.minecraft.player

import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import net.kyori.adventure.text.minimessage.MiniMessage
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.minecraft.events.DisconnectEvent
import ru.cherryngine.engine.minecraft.events.PacketEvent
import ru.cherryngine.engine.minecraft.events.PlayerConfigurationAsyncEvent
import ru.cherryngine.engine.minecraft.events.PlayerCreatedEvent
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.ServerConsts
import ru.cherryngine.lib.minecraft.network.Connection
import ru.cherryngine.lib.minecraft.network.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.common.ClientboundUpdateTagsPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.configurations.ClientboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.login.ServerboundLoginAcknowledgedPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound.*
import ru.cherryngine.lib.minecraft.network.protocol.packets.status.ClientboundStatusResponsePacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.status.ServerboundStatusRequestPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.network.protocol.types.ServerStatus
import ru.cherryngine.lib.minecraft.registry.Registries
import kotlinx.coroutines.channels.Channel
import java.util.*

@Singleton
class MinecraftConnectionService(
    private val playerManager: PlayerManager,
    val playerCreatedEventPublisher: ApplicationEventPublisher<PlayerCreatedEvent>,
    val playerConfigurationAsyncEventPublisher: ApplicationEventPublisher<PlayerConfigurationAsyncEvent>,
) {
    val packetChannel = Channel<Pair<UUID, ServerboundPacket>>(Channel.UNLIMITED)

    @EventListener
    fun onPacket(event: PacketEvent) {
        val (connection, packet) = event
        when (packet) {
            is ServerboundStatusRequestPacket -> {
                val status = ServerStatus(
                    version = ServerStatus.Version(
                        name = ServerConsts.MINECRAFT_VERSION,
                        protocol = ServerConsts.PROTOCOL_VERSION
                    ),
                    players = ServerStatus.Players(
                        online = 13,
                        max = 37,
                        sample = listOf(
                            ServerStatus.ServerListPlayer("test", UUID.randomUUID())
                        )
                    ),
                    description = MiniMessage.miniMessage().deserialize("<rainbow>Cherryngine</rainbow>")
                )
                connection.sendPacket(ClientboundStatusResponsePacket(status))
            }

            is ServerboundLoginAcknowledgedPacket -> {
                val cachedTagPacket = ClientboundUpdateTagsPacket(Registries.tagRegistries)
                connection.sendPacket(cachedTagPacket)

                Registries.dataDrivenRegistries.forEach { registry ->
                    connection.sendPacket(ClientboundRegistryDataPacket(registry))
                }

                val uuid = connection.gameProfile.uuid
                val existing = playerManager.getPlayerNullable(uuid)
                val player: MinecraftPlayer
                if (existing == null) {
                    player = MinecraftPlayer(connection)
                    playerManager.register(player)
                    playerCreatedEventPublisher.publishEvent(PlayerCreatedEvent(player))
                } else {
                    player = existing as MinecraftPlayer
                }

                Thread.startVirtualThread {
                    playerConfigurationAsyncEventPublisher.publishEvent(PlayerConfigurationAsyncEvent(player))
                    connection.sendPacket(ClientboundFinishConfigurationPacket())
                }
            }

            is ServerboundMovePlayerPosPacket -> onMove(
                connection,
                packet.pos, null, packet.flags
            )

            is ServerboundMovePlayerPosRotPacket -> onMove(
                connection,
                packet.pos, packet.yawPitch, packet.flags
            )

            is ServerboundMovePlayerRotPacket -> onMove(
                connection,
                null, packet.yawPitch, packet.flags
            )

            is ServerboundMovePlayerStatusOnlyPacket -> onMove(
                connection,
                null, null, packet.flags
            )

            is ServerboundPlayerInputPacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid) as? MinecraftPlayer ?: return
                player.isSneaking = true
            }
        }

        if (connection.state == ProtocolState.PLAY || connection.state == ProtocolState.CONFIGURATION) {
            packetChannel.trySend(connection.gameProfile.uuid to packet)
        }
    }

    private fun onMove(
        connection: Connection,
        pos: Vec3D?,
        yawPitch: YawPitch?,
        flags: MovePlayerFlags,
    ) {
        val player = playerManager.getPlayerNullable(connection.gameProfile.uuid) as? MinecraftPlayer ?: return
        if (pos != null) player.clientPosition = pos
        if (yawPitch != null) player.clientYawPitch = yawPitch
        player.clientMovePlayerFlags = flags
    }

    @EventListener
    fun onDisconnect(event: DisconnectEvent) {
        val connection = event.connection
        if (connection.state == ProtocolState.PLAY || connection.state == ProtocolState.CONFIGURATION) {
            val uuid = connection.gameProfile.uuid
            playerManager.unregister(uuid)
        }
    }
}
