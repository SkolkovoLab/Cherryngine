package ru.cherryngine.lib.minecraft.network.protocol.decoders

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import ru.cherryngine.lib.minecraft.network.ByteBufVarInt
import ru.cherryngine.lib.minecraft.network.protocol.NetworkCompression

class CompressionDecoder : ByteToMessageDecoder() {
    public override fun decode(connection: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        if (!connection.channel().isActive) return
        val dataLength = ByteBufVarInt.read(buffer)

        if (dataLength == 0) {
            out.add(buffer.retainedSlice())
            buffer.skipBytes(buffer.readableBytes())
            return
        }

        val compressed = ByteArray(buffer.readableBytes())
        buffer.readBytes(compressed)
        val uncompressed = Unpooled.wrappedBuffer(NetworkCompression.decompress(compressed))
        out.add(uncompressed)
    }
}
