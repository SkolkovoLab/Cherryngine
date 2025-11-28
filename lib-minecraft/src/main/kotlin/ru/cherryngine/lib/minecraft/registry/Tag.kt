package ru.cherryngine.lib.minecraft.registry

import io.netty.buffer.ByteBuf
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.BinaryTag
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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
                val registry = RegistryManager.getFromIdentifier<Registry<RegistryEntry>>(value.registryIdentifier)
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