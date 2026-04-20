package ru.cherryngine.lib.minecraft.network.protocol.decoders

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import ru.cherryngine.lib.minecraft.network.ByteBufVarInt
import java.io.IOException

class PacketLengthDecoder : ByteToMessageDecoder() {
    override fun decode(ctx: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        if (!ctx.channel().isActive) return

        buffer.markReaderIndex()
        val length = ByteBufVarInt.read(buffer)

        if (length > buffer.readableBytes()) {
            buffer.resetReaderIndex()
            return
        }

        out.add(buffer.retainedSlice(buffer.readerIndex(), length))
        buffer.skipBytes(length)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (cause is IOException) {
            ctx.close()
            return
        }
        super.exceptionCaught(ctx, cause)
    }
}
