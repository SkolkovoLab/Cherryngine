package ru.cherryngine.lib.minecraft.registry

import io.netty.buffer.ByteBuf
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.BinaryTag
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

@Serializable
data class Tag(
    val identifier: String,
    val values: Set<String>,
) : RegistryEntry {
    override fun getNbt(): BinaryTag? = null

    override fun getEntryIdentifier(): String {
        return identifier
    }

    operator fun contains(identifier: String): Boolean {
        return values.contains(identifier)
    }

    override fun toString(): String {
        return "#$identifier"
    }

    fun unwrappedValues(registry: TagRegistry): Set<String> {
        val result = mutableSetOf<String>()
        values.forEach { value ->
            if (value.startsWith('#')) {
                result += registry[value.removePrefix("#")].unwrappedValues(registry)
            } else {
                result += value
            }
        }
        return result
    }

    class TagStreamCodec(
        val tagRegistry: TagRegistry,
    ) : StreamCodec<Tag> {
        override fun write(buffer: ByteBuf, value: Tag) {
            StreamCodec.STRING.write(buffer, value.identifier)
            @Suppress("UNCHECKED_CAST")
            val registry = tagRegistry.parentRegistry as Registry<RegistryEntry>
            val intTags = value.unwrappedValues(tagRegistry).map { tag ->
                val entry = registry[tag]
                registry.getProtocolIdByEntry(entry)
            }
            StreamCodec.VAR_INT.list().write(buffer, intTags)
        }

        override fun read(buffer: ByteBuf): Tag {
            TODO("Not yet implemented")
        }
    }
}