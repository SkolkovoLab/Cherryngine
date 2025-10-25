package ru.cherryngine.lib.minecraft.extentions

import io.netty.channel.ChannelHandlerContext
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.server.Connection


fun ChannelHandlerContext.sendPacket(packet: ClientboundPacket, processor: Connection) {
    this.writeAndFlush(packet)
}