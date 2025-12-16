package ru.cherryngine.lib.minecraft.network.stream_codec

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.utils.registry.Registry

class RegistryStreamCodec<T : Any>(
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