package ru.cherryngine.engine.minecraft.events

import net.minestom.server.network.packet.client.ClientPacket
import ru.cherryngine.lib.minecraft.network.Connection

data class PacketEvent(
    val connection: Connection,
    val packet: ClientPacket,
)
