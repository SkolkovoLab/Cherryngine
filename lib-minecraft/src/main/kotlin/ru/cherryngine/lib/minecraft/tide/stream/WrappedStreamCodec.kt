package ru.cherryngine.lib.minecraft.tide.stream

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class WrappedStreamCodec<T>(
    val inner: StreamCodec<T>
) : StreamCodec<T> {
    override fun write(buffer: ByteBuf, value: T) {
        val innerBuffer = Unpooled.buffer()
        inner.write(innerBuffer, value)
        StreamCodec.BYTE_BUF.write(buffer, innerBuffer)
    }

    override fun read(buffer: ByteBuf): T {
        val innerBuffer = StreamCodec.BYTE_BUF.read(buffer)
        return inner.read(innerBuffer)
    }
}