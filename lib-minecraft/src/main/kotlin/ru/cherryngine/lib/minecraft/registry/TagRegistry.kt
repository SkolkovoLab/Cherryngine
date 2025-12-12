package ru.cherryngine.lib.minecraft.registry

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

abstract class TagRegistry(
    identifier: String,
    resource: String,
    val parentRegistry: Registry<*>,
) : KtJsonDataDrivenRegistry<Tag>(identifier, resource, Tag.serializer()) {
    val reversed by lazy {
        entries.keyToValue().values
            .flatMap { value -> value.values.map { it to value.identifier } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.toSet() }
    }

    companion object {
        val STREAM_CODEC = object : StreamCodec<TagRegistry> {
            override fun write(buffer: ByteBuf, value: TagRegistry) {
                StreamCodec.STRING.write(buffer, value.identifier)
                val list = value.getEntries().keyToValue().values.toList()
                Tag.TagStreamCodec(value).list().write(buffer, list)
            }

            override fun read(buffer: ByteBuf): TagRegistry {
                TODO("Not yet implemented")
            }
        }
    }
}

