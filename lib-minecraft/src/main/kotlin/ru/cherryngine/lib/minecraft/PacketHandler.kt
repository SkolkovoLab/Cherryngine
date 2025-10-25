package ru.cherryngine.lib.minecraft

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.server.Connection

interface PacketHandler {
    fun onPacket(connection: Connection, packet: ServerboundPacket) {

    }
}