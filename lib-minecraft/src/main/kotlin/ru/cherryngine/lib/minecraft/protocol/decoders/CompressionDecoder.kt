package ru.cherryngine.lib.minecraft.protocol.decoders

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import ru.cherryngine.lib.minecraft.protocol.NetworkCompression
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class CompressionDecoder() : ByteToMessageDecoder() {
    override fun decode(connection: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        if (!connection.channel().isActive) return
        val dataLength = StreamCodec.VAR_INT.read(buffer)

        if (dataLength == 0) {
            out.add(buffer.retainedSlice())
            buffer.skipBytes(buffer.readableBytes())
            return
        }

        val compressed = StreamCodec.RAW_BYTES_ARRAY.read(buffer)
        val uncompressed = Unpooled.wrappedBuffer(NetworkCompression.decompress(compressed))
        out.add(uncompressed)
    }
}