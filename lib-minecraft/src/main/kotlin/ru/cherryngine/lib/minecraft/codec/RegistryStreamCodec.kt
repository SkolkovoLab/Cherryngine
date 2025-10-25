package ru.cherryngine.lib.minecraft.codec

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.registry.Registry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class RegistryStreamCodec<T : RegistryEntry>(
    val registry: Registry<T>
) : StreamCodec<T> {
    override fun write(buffer: ByteBuf, value: T) {
        val id = registry.getProtocolIdByEntry(value)
        StreamCodec.VAR_INT.write(buffer, id)
    }

    override fun read(buffer: ByteBuf): T {
        val id = StreamCodec.VAR_INT.read(buffer)
        return registry.getByProtocolId(id)
    }
}