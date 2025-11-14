package ru.cherryngine.lib.grimac.packetevents.handlers

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.util.EventCreationUtil
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import ru.cherryngine.lib.minecraft.server.Connection

@ChannelHandler.Sharable
class PacketEventsDecoder(var user: User) : MessageToMessageDecoder<ByteBuf>() {
    var player: Connection? = null

    fun read(ctx: ChannelHandlerContext, byteBuf: ByteBuf, output: MutableList<Any>) {
        val transformed = ctx.alloc().buffer().writeBytes(byteBuf)
        try {
            val firstReaderIndex = transformed.readerIndex()
            val packetReceiveEvent = EventCreationUtil.createReceiveEvent(
                ctx.channel(), user, player, transformed, false
            )

            val readerIndex = transformed.readerIndex()
            PacketEvents.getAPI().eventManager.callEvent(packetReceiveEvent) {
                transformed.readerIndex(readerIndex)
            }

            if (!packetReceiveEvent.isCancelled) {
                val lastUsedWrapper = packetReceiveEvent.lastUsedWrapper
                if (lastUsedWrapper != null) {
                    ByteBufHelper.clear(packetReceiveEvent.byteBuf)
                    lastUsedWrapper.writeVarInt(packetReceiveEvent.packetId)
                    lastUsedWrapper.write()
                }

                transformed.readerIndex(firstReaderIndex)
                output.add(transformed.retain())
            }

            if (packetReceiveEvent.hasPostTasks()) {
                for (task in packetReceiveEvent.postTasks) {
                    task.run()
                }
            }
        } finally {
            transformed.release()
        }
    }

    override fun decode(ctx: ChannelHandlerContext, byteBuf: ByteBuf, out: MutableList<Any>) {
        if (byteBuf.isReadable) {
            read(ctx, byteBuf, out)
        }
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext, event: Any) {
        super.userEventTriggered(ctx, event)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        ctx.channel().pipeline().remove(PacketEvents.DECODER_NAME)
        ctx.channel().pipeline().remove(PacketEvents.ENCODER_NAME)
        super.channelInactive(ctx)
    }
}
