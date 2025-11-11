package ru.cherryngine.lib.minecraft.protocol.encoders

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.protocol.NetworkCompression
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class CompressionEncoder(
    val compressionThreshold: Int,
) : MessageToByteEncoder<ByteBuf>() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun encode(connection: ChannelHandlerContext, buffer: ByteBuf, out: ByteBuf) {
        try {
            val dataLength = buffer.readableBytes()
            if (dataLength < compressionThreshold) {
                StreamCodec.VAR_INT.write(out, 0)
                out.writeBytes(buffer)
            } else {
                StreamCodec.VAR_INT.write(out, dataLength)
                out.writeBytes(NetworkCompression.compress(StreamCodec.RAW_BYTES_ARRAY.read(buffer)))
            }
        } catch (exception: Exception) {
            logger.error("There was an error while compressing packet", exception)
        }
    }
}