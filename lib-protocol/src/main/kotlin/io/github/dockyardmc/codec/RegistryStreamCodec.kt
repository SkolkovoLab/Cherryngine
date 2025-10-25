package io.github.dockyardmc.codec

import io.github.dockyardmc.registry.Registry
import io.github.dockyardmc.registry.RegistryEntry
import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf

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