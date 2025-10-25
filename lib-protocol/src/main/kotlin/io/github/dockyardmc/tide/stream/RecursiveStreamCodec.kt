package io.github.dockyardmc.tide.stream

import io.netty.buffer.ByteBuf

class RecursiveStreamCodec<T>(
    self: (StreamCodec<T>) -> StreamCodec<T>
) : StreamCodec<T> {
    val delegate = self.invoke(this)

    override fun write(buffer: ByteBuf, value: T) {
        delegate.write(buffer, value)
    }

    override fun read(buffer: ByteBuf): T {
        return delegate.read(buffer)
    }

    companion object {
        fun <T> recursive(self: (StreamCodec<T>) -> StreamCodec<T>): RecursiveStreamCodec<T> {
            return RecursiveStreamCodec<T>(self)
        }
    }
}