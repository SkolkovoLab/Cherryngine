package io.github.dockyardmc.protocol.decoders

import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.slf4j.LoggerFactory

class PacketLengthDecoder : ByteToMessageDecoder() {

    override fun decode(connection: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        if (!connection.channel().isActive) return

        buffer.markReaderIndex()
        val length = StreamCodec.VAR_INT.read(buffer)

        // reset the reader index if we don't have enough bytes and wait for next part of the message to arrive and check again
        if (length > buffer.readableBytes()) {
            buffer.resetReaderIndex()
            return
        }

        out.add(buffer.retainedSlice(buffer.readerIndex(), length))
        buffer.skipBytes(length)
        // +1 to account for the size byte that is not counted
//        ServerMetrics.inboundBandwidth.add(length + 1, DataSizeCounter.Type.BYTE)
//        ServerMetrics.totalBandwidth.add(length + 1, DataSizeCounter.Type.BYTE)
    }

    override fun exceptionCaught(connection: ChannelHandlerContext, cause: Throwable) {
        connection.channel().close().sync()
        if (cause.message == "An established connection was aborted by the software in your host machine") return

        LoggerFactory.getLogger(PacketLengthDecoder::class.java).error("Error occurred while decoding frame", cause)
    }
}