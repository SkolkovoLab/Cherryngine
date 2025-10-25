package io.github.dockyardmc.protocol.encoders

import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.LoggerFactory

class PacketLengthEncoder : MessageToByteEncoder<ByteBuf>() {
    private val logger = LoggerFactory.getLogger(PacketLengthEncoder::class.java)

    override fun encode(connection: ChannelHandlerContext, buffer: ByteBuf, out: ByteBuf) {
        try {
            val size = buffer.readableBytes()
            StreamCodec.VAR_INT.write(out, size)
            out.writeBytes(buffer)

            // +1 to account for the size byte that is not counted
//            ServerMetrics.outboundBandwidth.add(size + 1, DataSizeCounter.Type.BYTE)
//            ServerMetrics.totalBandwidth.add(size + 1, DataSizeCounter.Type.BYTE)
        } catch (exception: Exception) {
            logger.error("There was an error while encoding packet length", exception)
        }
    }
}