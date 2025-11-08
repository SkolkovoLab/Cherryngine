package ru.cherryngine.lib.minecraft.registry.registries.tags

import io.netty.buffer.ByteBuf
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.BinaryTag
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.Registry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.registry.RegistryManager
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

abstract class TagRegistry : DataDrivenRegistry<Tag>() {
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

@Serializable
data class Tag(
    val identifier: String,
    val tags: Set<String>,
    val registryIdentifier: String,
) : RegistryEntry {
    override fun getNbt(): BinaryTag? = null

    override fun getEntryIdentifier(): String {
        return identifier
    }

    operator fun contains(identifier: String): Boolean {
        return tags.contains(identifier)
    }

    override fun toString(): String {
        return "#$identifier"
    }

    companion object {
        val STREAM_CODEC = object : StreamCodec<Tag> {
            override fun write(buffer: ByteBuf, value: Tag) {
                @Suppress("UNCHECKED_CAST")
                val registry = RegistryManager.getFromIdentifier<Registry<*>>(value.registryIdentifier) as Registry<Any>
                StreamCodec.STRING.write(buffer, value.identifier)
                val intTags = value.tags.map { tag ->
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
}