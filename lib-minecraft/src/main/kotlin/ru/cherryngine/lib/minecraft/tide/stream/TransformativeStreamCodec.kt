package ru.cherryngine.lib.minecraft.tide.stream

import io.netty.buffer.ByteBuf

open class TransformativeStreamCodec<T, S>(
    val inner: StreamCodec<T>,
    val to: (T) -> S,
    val from: (S) -> T,
) : StreamCodec<S> {
    override fun write(buffer: ByteBuf, value: S) {
        inner.write(buffer, from.invoke(value))
    }

    override fun read(buffer: ByteBuf): S {
        val innerValue = inner.read(buffer)
        return to.invoke(innerValue)
    }
}