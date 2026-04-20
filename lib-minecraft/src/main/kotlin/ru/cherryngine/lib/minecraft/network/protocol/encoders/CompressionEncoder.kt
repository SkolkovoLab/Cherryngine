package ru.cherryngine.lib.minecraft.network.protocol.encoders

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.network.ByteBufVarInt
import ru.cherryngine.lib.minecraft.network.protocol.NetworkCompression

class CompressionEncoder(
    val compressionThreshold: Int,
) : MessageToByteEncoder<ByteBuf>() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun encode(connection: ChannelHandlerContext, buffer: ByteBuf, out: ByteBuf) {
        try {
            val dataLength = buffer.readableBytes()
            if (dataLength < compressionThreshold) {
                ByteBufVarInt.write(out, 0)
                out.writeBytes(buffer)
            } else {
                ByteBufVarInt.write(out, dataLength)
                val uncompressed = ByteArray(dataLength)
                buffer.readBytes(uncompressed)
                out.writeBytes(NetworkCompression.compress(uncompressed))
            }
        } catch (exception: Exception) {
            logger.error("There was an error while compressing packet", exception)
        }
    }
}
