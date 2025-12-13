package ru.cherryngine.lib.minecraft.r2

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class Registry2StreamCodec<T : Any>(
    val registry: Registry<T>,
) : StreamCodec<T> {
    override fun write(buffer: ByteBuf, value: T) {
        val id = registry.getId(value)
        StreamCodec.VAR_INT.write(buffer, id)
    }

    override fun read(buffer: ByteBuf): T {
        val id = StreamCodec.VAR_INT.read(buffer)
        return registry[id]
    }
}