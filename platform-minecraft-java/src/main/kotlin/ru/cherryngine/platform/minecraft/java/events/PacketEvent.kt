package ru.cherryngine.platform.minecraft.java.events

import net.minestom.server.network.packet.client.ClientPacket
import ru.cherryngine.platform.minecraft.java.network.Connection

data class PacketEvent(
    val connection: Connection,
    val packet: ClientPacket,
)
