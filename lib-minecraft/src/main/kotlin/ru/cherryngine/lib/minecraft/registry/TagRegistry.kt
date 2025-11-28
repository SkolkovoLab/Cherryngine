package ru.cherryngine.lib.minecraft.registry

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

abstract class TagRegistry(
    identifier: String,
    resource: String,
) : DataDrivenRegistry<Tag>(identifier, resource, Tag.serializer()) {
    companion object {
        val STREAM_CODEC = object : StreamCodec<TagRegistry> {
            override fun write(buffer: ByteBuf, value: TagRegistry) {
                StreamCodec.STRING.write(buffer, value.identifier)
                val list = value.getEntries().keyToValue().values.toList()
                Tag.STREAM_CODEC.list().write(buffer, list)
            }

            override fun read(buffer: ByteBuf): TagRegistry {
                TODO("Not yet implemented")
            }
        }
    }
}

