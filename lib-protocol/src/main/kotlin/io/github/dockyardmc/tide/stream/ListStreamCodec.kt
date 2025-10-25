package io.github.dockyardmc.tide.stream

import io.netty.buffer.ByteBuf

class ListStreamCodec<T>(
    val inner: StreamCodec<T>
) : StreamCodec<List<T>> {
    override fun write(buffer: ByteBuf, value: List<T>) {
        StreamCodec.VAR_INT.write(buffer, value.size)
        value.forEach { inner.write(buffer, it) }
    }

    override fun read(buffer: ByteBuf): List<T> {
        val size = StreamCodec.VAR_INT.read(buffer)
        return List(size) { inner.read(buffer) }
    }
}