package ru.cherryngine.engine.core.events

import ru.cherryngine.lib.minecraft.network.Connection
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket

data class PacketEvent(
    val connection: Connection,
    val packet: ServerboundPacket,
)