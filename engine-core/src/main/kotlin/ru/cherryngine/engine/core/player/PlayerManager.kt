package ru.cherryngine.engine.core.player

import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import net.kyori.adventure.text.minimessage.MiniMessage
import ru.cherryngine.engine.core.events.DisconnectEvent
import ru.cherryngine.engine.core.events.PacketEvent
import ru.cherryngine.engine.core.events.PlayerConfigurationAsyncEvent
import ru.cherryngine.engine.core.events.PlayerCreatedEvent
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.ServerConsts
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundUpdateTagsPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundLoginAcknowledgedPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.*
import ru.cherryngine.lib.minecraft.protocol.packets.status.ClientboundStatusResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.status.ServerboundStatusRequestPacket
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.protocol.types.ServerStatus
import ru.cherryngine.lib.minecraft.registry.RegistryManager
import ru.cherryngine.lib.minecraft.registry.registries.tags.*
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Singleton
class PlayerManager(
    val playerCreatedEventPublisher: ApplicationEventPublisher<PlayerCreatedEvent>,
    val playerConfigurationAsyncEventPublisher: ApplicationEventPublisher<PlayerConfigurationAsyncEvent>,
) {
    val queues: MutableMap<UUID, MutableList<ServerboundPacket>> =
        ConcurrentHashMap<UUID, MutableList<ServerboundPacket>>()
    val toCreatePlayers = mutableSetOf<UUID>()
    val toRemovePlayers = mutableSetOf<UUID>()

    private val playersByUUID: MutableMap<UUID, Player> = ConcurrentHashMap()
    private val playersByUsername: MutableMap<String, Player> = ConcurrentHashMap()

    fun getPlayerNullable(uuid: UUID): Player? {
        return playersByUUID[uuid]
    }

    fun getPlayerNullable(username: String): Player? {
        return playersByUsername[username.lowercase()]
    }

    fun getPlayer(uuid: UUID): Player {
        return playersByUUID[uuid] ?: throw NullPointerException("Player $uuid not found")
    }

    fun getPlayer(connection: Connection): Player {
        return getPlayer(connection.gameProfile.uuid)
    }

    fun onlinePlayers() = playersByUUID.values.toList()

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

                val uuid = connection.gameProfile.uuid
                val username = connection.gameProfile.username
                val player: Player
                if (uuid !in playersByUUID) {
                    player = Player(connection)
                    playersByUUID[uuid] = player
                    playersByUsername[username.lowercase()] = player
                    toCreatePlayers.add(uuid)
                    playerCreatedEventPublisher.publishEvent(PlayerCreatedEvent(player))
                } else {
                    player = playersByUUID[uuid]!!
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
                val player = playersByUUID[connection.gameProfile.uuid] ?: return
                player.isSneaking = true
            }
        }

        if (connection.state == ProtocolState.PLAY || connection.state == ProtocolState.CONFIGURATION) {
            val queue = queues.computeIfAbsent(connection.gameProfile.uuid) { arrayListOf() }
            queue.add(packet)
        }
    }

    private fun onMove(
        connection: Connection,
        pos: Vec3D?,
        yawPitch: YawPitch?,
        flags: MovePlayerFlags,
    ) {
        val player = playersByUUID[connection.gameProfile.uuid] ?: return
        if (pos != null) player.clientPosition = pos
        if (yawPitch != null) player.clientYawPitch = yawPitch
        player.clientMovePlayerFlags = flags
    }

    @EventListener
    fun onDisconnect(event: DisconnectEvent) {
        val connection = event.connection
        if (connection.state == ProtocolState.PLAY || connection.state == ProtocolState.CONFIGURATION) {
            val uuid = connection.gameProfile.uuid
            val username = connection.gameProfile.username
            toRemovePlayers.add(uuid)
            playersByUUID.remove(uuid)
            playersByUsername.remove(username.lowercase())
        }
    }
}