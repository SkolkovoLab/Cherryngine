package io.github.dockyardmc

import io.github.dockyardmc.server.Connection
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.netty.channel.ChannelHandlerContext

interface PacketHandler {
    fun onPacket(connection: Connection, packet: ServerboundPacket) {

    }
}