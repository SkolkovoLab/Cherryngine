package ru.cherryngine.impl.demo

import net.kyori.adventure.text.minimessage.MiniMessage
import ru.cherryngine.lib.minecraft.PacketHandler
import ru.cherryngine.lib.minecraft.ServerConsts
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundUpdateTagsPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundLoginAcknowledgedPacket
import ru.cherryngine.lib.minecraft.protocol.packets.status.ClientboundStatusResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.status.ServerboundStatusRequestPacket
import ru.cherryngine.lib.minecraft.protocol.types.ServerStatus
import ru.cherryngine.lib.minecraft.registry.RegistryManager
import ru.cherryngine.lib.minecraft.registry.registries.tags.*
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class DemoPacketHandler(
    val defaultViewContextID: String,
) : PacketHandler {
    val queues: MutableMap<UUID, MutableList<ServerboundPacket>> =
        ConcurrentHashMap<UUID, MutableList<ServerboundPacket>>()
    val toCreatePlayers = mutableSetOf<UUID>()
    val toRemovePlayers = mutableSetOf<UUID>()

    val players: MutableMap<UUID, Player> = ConcurrentHashMap()

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
                val uuid = connection.gameProfile.uuid
                players.computeIfAbsent(uuid) { Player(connection) }
                toCreatePlayers.add(uuid)

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

            else -> {
                if (connection.state == ProtocolState.PLAY || connection.state == ProtocolState.CONFIGURATION) {
                    val queue = queues.computeIfAbsent(connection.gameProfile.uuid) { arrayListOf() }
                    queue.add(packet)
                }
            }
        }
    }

    override fun onDisconnect(connection: Connection) {
        if (connection.state == ProtocolState.PLAY || connection.state == ProtocolState.CONFIGURATION) {
            val uuid = connection.gameProfile.uuid
            toRemovePlayers.add(uuid)
            players.remove(uuid)
        }
    }
}