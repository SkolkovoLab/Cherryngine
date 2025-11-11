package ru.cherryngine.lib.minecraft.tide.stream

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
        val byteArray = ByteArray(length)
        buffer.readBytes(byteArray)
        return byteArray
    }
}