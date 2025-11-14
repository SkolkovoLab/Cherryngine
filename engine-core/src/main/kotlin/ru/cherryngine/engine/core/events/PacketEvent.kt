package ru.cherryngine.engine.core.events

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.server.Connection

data class PacketEvent(
    val connection: Connection,
    val packet: ServerboundPacket,
)