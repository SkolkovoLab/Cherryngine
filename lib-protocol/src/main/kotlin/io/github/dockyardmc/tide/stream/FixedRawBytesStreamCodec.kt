package io.github.dockyardmc.tide.stream

import io.github.dockyardmc.tide.codec.CodecUtils.toByteArraySafe
import io.netty.buffer.ByteBuf

class FixedRawBytesStreamCodec(
    val length: Int
) : StreamCodec<ByteArray> {
    override fun write(buffer: ByteBuf, value: ByteArray) {
        require(value.size == length) {
            "Expected ByteArray of size $length, but got ${value.size}"
        }
        buffer.writeBytes(value)
    }

    override fun read(buffer: ByteBuf): ByteArray {
        require(buffer.readableBytes() >= length) {
            "Not enough bytes in buffer. Expected $length, but got ${buffer.readableBytes()}"
        }
        val buf = buffer.readBytes(length)
        val byteArray = buf.toByteArraySafe()
        buf.release()
        return byteArray
    }
}