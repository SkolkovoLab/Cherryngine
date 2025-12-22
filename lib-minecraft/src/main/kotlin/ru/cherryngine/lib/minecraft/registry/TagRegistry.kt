package ru.cherryngine.lib.minecraft.registry

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.registry.Registry
import ru.cherryngine.lib.minecraft.utils.toKey

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

        @OptIn(ExperimentalSerializationApi::class)
        fun readTags(resourceName: String): Map<Key, List<Key>> {
            val resource = ClassLoader.getSystemResource(resourceName)
                ?: throw IllegalStateException("Resource $resourceName not found")

            val map: Map<String, JsonObject> = resource.openStream().use {
                Json.decodeFromStream<Map<String, JsonObject>>(it)
            }

            fun getRealValues(value: String): List<Key> {
                if (!value.startsWith('#')) return listOf(value.toKey())
                val key = value.substring(1)
                return map[key]!!["values"]!!.jsonArray
                    .map { it.jsonPrimitive.content }
                    .flatMap { getRealValues(it) }
            }

            val tags = map.keys.associate {
                it.toKey() to getRealValues("#$it")
            }

            return tags
        }

        fun create(
            tags: Map<Key, List<Key>>,
            registry: Registry<*>,
        ): TagRegistry {
            val tags = tags.entries.map { (key, value) ->
                Tag(key, value.map { registry[it].id })
            }
            return TagRegistry(registry.key, tags)
        }

        fun create(resourceName: String, registry: Registry<*>): TagRegistry {
            return create(readTags(resourceName), registry)
        }
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