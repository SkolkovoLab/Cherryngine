package io.github.dockyardmc.extentions

import io.github.dockyardmc.server.Connection
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.netty.channel.ChannelHandlerContext


fun ChannelHandlerContext.sendPacket(packet: ClientboundPacket, processor: Connection) {
    this.writeAndFlush(packet)
}