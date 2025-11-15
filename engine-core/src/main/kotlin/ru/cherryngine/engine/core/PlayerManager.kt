package ru.cherryngine.engine.core

import io.micronaut.context.event.ApplicationEventPublisher
import jakarta.inject.Singleton
import net.kyori.adventure.text.minimessage.MiniMessage
import ru.cherryngine.engine.core.events.PacketEvent
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.PacketHandler
import ru.cherryngine.lib.minecraft.ServerConsts
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundUpdateTagsPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ServerboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundLoginAcknowledgedPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerPosPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerPosRotPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerRotPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerStatusOnlyPacket
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
    val packetEventPublisher: ApplicationEventPublisher<PacketEvent>,
) : PacketHandler {
    val queues: MutableMap<UUID, MutableList<ServerboundPacket>> =
        ConcurrentHashMap<UUID, MutableList<ServerboundPacket>>()
    val toCreatePlayers = mutableSetOf<UUID>()
    val toRemovePlayers = mutableSetOf<UUID>()

    val playersByUUID: MutableMap<UUID, Player> = ConcurrentHashMap()
    val playersByUsername: MutableMap<String, Player> = ConcurrentHashMap()

    fun getPlayerNullable(uuid: UUID): Player? {
        return playersByUUID[uuid]
    }

    fun getPlayerNullable(username: String): Player? {
        return playersByUsername[username]
    }

    fun getPlayer(uuid: UUID): Player {
        return playersByUUID[uuid] ?: throw NullPointerException("Player $uuid not found")
    }

    fun getPlayer(connection: Connection): Player {
        return getPlayer(connection.gameProfile.uuid)
    }

    fun onlinePlayers() = playersByUUID.values.toList()

    override fun onPacket(connection: Connection, packet: ServerboundPacket) {
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
                connection.sendPacket(ClientboundFinishConfigurationPacket())
            }

            is ServerboundFinishConfigurationPacket -> {
                val uuid = connection.gameProfile.uuid
                val username = connection.gameProfile.username
                playersByUUID.computeIfAbsent(uuid) { Player(connection) }
                playersByUsername.computeIfAbsent(username) { Player(connection) }
                toCreatePlayers.add(uuid)
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
        }

        if (connection.state == ProtocolState.PLAY || connection.state == ProtocolState.CONFIGURATION) {
            val queue = queues.computeIfAbsent(connection.gameProfile.uuid) { arrayListOf() }
            queue.add(packet)
        }
        packetEventPublisher.publishEvent(PacketEvent(connection, packet))
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

    override fun onDisconnect(connection: Connection) {
        if (connection.state == ProtocolState.PLAY || connection.state == ProtocolState.CONFIGURATION) {
            val uuid = connection.gameProfile.uuid
            val username = connection.gameProfile.username
            toRemovePlayers.add(uuid)
            playersByUUID.remove(uuid)
            playersByUsername.remove(username)
        }
    }
}