package ru.cherryngine.lib.minecraft.tide.stream

import io.netty.buffer.ByteBuf

class DefaultStreamCodec<T>(
    val inner: StreamCodec<T>,
    val default: T
) : StreamCodec<T> {
    override fun write(buffer: ByteBuf, value: T) {
        StreamCodec.BOOLEAN.write(buffer, true)
        val valueToWrite = value ?: default
        inner.write(buffer, valueToWrite)
    }

    override fun read(buffer: ByteBuf): T {
        val isNull = StreamCodec.BOOLEAN.read(buffer)
        return if (!isNull) inner.read(buffer) else default
    }
}