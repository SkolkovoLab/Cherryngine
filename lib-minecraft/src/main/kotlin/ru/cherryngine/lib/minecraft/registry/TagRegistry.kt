package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class TagRegistry(
    val key: Key,
    val tags: List<Tag>,
) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY, TagRegistry::key,
            Tag.STREAM_CODEC.list(), TagRegistry::tags,
            ::TagRegistry
        )
    }

    data class Tag(
        val key: Key,
        val entries: List<Int>,
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.KEY, Tag::key,
                StreamCodec.VAR_INT.list(), Tag::entries,
                ::Tag
            )
        }
    }
}