package io.github.dockyardmc.registry.registries.tags

import io.github.dockyardmc.registry.DataDrivenRegistry
import io.github.dockyardmc.registry.Registry
import io.github.dockyardmc.registry.RegistryEntry
import io.github.dockyardmc.registry.RegistryManager
import io.github.dockyardmc.registry.registries.BlockRegistry
import io.github.dockyardmc.registry.registries.RegistryBlock
import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.BinaryTag

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
    override fun getProtocolId(): Int = -1

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
                val registry = RegistryManager.getFromIdentifier<Registry<*>>(value.registryIdentifier)
                StreamCodec.STRING.write(buffer, value.identifier)
                val intTags = value.tags.map { tag ->
                    val entry = registry[tag]
                    if (registry is BlockRegistry) (entry as RegistryBlock).getLegacyProtocolId() else entry.getProtocolId()
                }
                StreamCodec.VAR_INT.list().write(buffer, intTags)
            }

            override fun read(buffer: ByteBuf): Tag {
                TODO("Not yet implemented")
            }
        }
    }
}