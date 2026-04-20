package ru.cherryngine.engine.minecraft.player

import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import net.minestom.server.coordinate.Pos
import net.minestom.server.network.ConnectionState
import net.minestom.server.network.packet.client.common.ClientPingRequestPacket
import net.minestom.server.network.packet.client.configuration.ClientFinishConfigurationPacket
import net.minestom.server.network.packet.client.login.ClientLoginAcknowledgedPacket
import net.minestom.server.network.packet.client.play.ClientCommandChatPacket
import net.minestom.server.network.packet.client.play.ClientInputPacket
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket
import net.minestom.server.network.packet.client.play.ClientPlayerPositionStatusPacket
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket
import net.minestom.server.network.packet.client.play.ClientTabCompletePacket
import net.minestom.server.network.packet.client.status.StatusRequestPacket
import net.minestom.server.network.packet.server.configuration.FinishConfigurationPacket
import net.minestom.server.network.packet.server.status.ResponsePacket
import ru.cherryngine.engine.core.player.InstanceRouter
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerRouter
import ru.cherryngine.engine.minecraft.events.DisconnectEvent
import ru.cherryngine.engine.minecraft.events.PacketEvent
import ru.cherryngine.engine.minecraft.events.PlayerConfigurationAsyncEvent
import ru.cherryngine.engine.minecraft.events.PlayerCreatedEvent
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.ServerConsts
import ru.cherryngine.lib.minecraft.network.Connection
import ru.cherryngine.lib.minecraft.world.MovePlayerFlags

@Singleton
class MinecraftConnectionService(
    private val playerManager: PlayerManager,
    private val instanceRouter: InstanceRouter,
    private val playerRouter: PlayerRouter,
    val playerCreatedEventPublisher: ApplicationEventPublisher<PlayerCreatedEvent>,
    val playerConfigurationAsyncEventPublisher: ApplicationEventPublisher<PlayerConfigurationAsyncEvent>,
) {
    companion object {
        private const val STATUS_JSON_TEMPLATE = """{"version":{"name":"%s","protocol":%d},"players":{"online":13,"max":37,"sample":[]},"description":{"text":"Cherryngine"}}"""
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        val (connection, packet) = event
        when (packet) {
            is StatusRequestPacket -> {
                val json = STATUS_JSON_TEMPLATE.format(ServerConsts.MINECRAFT_VERSION, ServerConsts.PROTOCOL_VERSION)
                connection.sendPacket(ResponsePacket(json))
            }

            is ClientLoginAcknowledgedPacket -> {
                // TODO: отправка TagsPacket / RegistryDataPacket требует заполненных Minestom Registries.
                //  Пока шлём сразу FinishConfigurationPacket, что работает только при очень
                //  снисходительном клиенте/прокси. В задаче #6 поднимем дефолтные registries.
                val uuid = connection.gameProfile.uuid()
                val existing = playerManager.getPlayerNullable(uuid)
                val player: MinecraftPlayer = if (existing == null) {
                    val created = MinecraftPlayer(connection)
                    playerManager.register(created)
                    playerCreatedEventPublisher.publishEvent(PlayerCreatedEvent(created))
                    created
                } else {
                    existing as MinecraftPlayer
                }

                Thread.startVirtualThread {
                    playerConfigurationAsyncEventPublisher.publishEvent(PlayerConfigurationAsyncEvent(player))
                    connection.sendPacket(FinishConfigurationPacket())
                }
            }

            is ClientFinishConfigurationPacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) ?: return
                instanceRouter.routePlayer(player.uuid, playerRouter.getInitialInstance(player))
            }

            is ClientPlayerPositionPacket -> onMove(
                connection,
                Vec3D(packet.position.x(), packet.position.y(), packet.position.z()),
                null,
                flagsFromByte(packet.flags),
            )

            is ClientPlayerPositionAndRotationPacket -> {
                val p: Pos = packet.position
                onMove(
                    connection,
                    Vec3D(p.x(), p.y(), p.z()),
                    YawPitch(p.yaw(), p.pitch()),
                    flagsFromByte(packet.flags),
                )
            }

            is ClientPlayerRotationPacket -> onMove(
                connection,
                null,
                YawPitch(packet.yaw, packet.pitch),
                flagsFromByte(packet.flags),
            )

            is ClientPlayerPositionStatusPacket -> onMove(
                connection,
                null, null, flagsFromByte(packet.flags),
            )

            is ClientCommandChatPacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) as? MinecraftPlayer ?: return
                player.pendingCommands.offer(packet.message)
            }

            is ClientTabCompletePacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) as? MinecraftPlayer ?: return
                player.pendingSuggestions.offer(Pair(packet.transactionId, packet.text.removePrefix("/")))
            }

            is ClientInputPacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) as? MinecraftPlayer ?: return
                player.isSneaking = true
            }

            is ClientPingRequestPacket -> {
                // отвечает за Connection, игнорируем здесь
            }
        }
    }

    private fun flagsFromByte(flags: Byte): MovePlayerFlags {
        val i = flags.toInt()
        return MovePlayerFlags(isOnGround = (i and 0x01) != 0, horizontalCollision = (i and 0x02) != 0)
    }

    private fun onMove(
        connection: Connection,
        pos: Vec3D?,
        yawPitch: YawPitch?,
        flags: MovePlayerFlags,
    ) {
        val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) as? MinecraftPlayer ?: return
        if (pos != null) player.clientPosition = pos
        if (yawPitch != null) player.clientYawPitch = yawPitch
        player.clientMovePlayerFlags = flags
    }

    @EventListener
    fun onDisconnect(event: DisconnectEvent) {
        val connection = event.connection
        if (connection.state == ConnectionState.PLAY || connection.state == ConnectionState.CONFIGURATION) {
            val uuid = connection.gameProfile.uuid()
            val player = playerManager.getPlayerNullable(uuid)
            if (player != null) {
                instanceRouter.removePlayer(player)
            }
            playerManager.unregister(uuid)
        }
    }
}
