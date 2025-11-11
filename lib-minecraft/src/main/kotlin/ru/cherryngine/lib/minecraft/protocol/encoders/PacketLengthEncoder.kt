package ru.cherryngine.lib.minecraft.protocol.encoders

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class PacketLengthEncoder : MessageToByteEncoder<ByteBuf>() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun encode(connection: ChannelHandlerContext, buffer: ByteBuf, out: ByteBuf) {
        try {
            val size = buffer.readableBytes()
            StreamCodec.VAR_INT.write(out, size)
            out.writeBytes(buffer)
        } catch (exception: Exception) {
            logger.error("There was an error while encoding packet length", exception)
        }
    }
}