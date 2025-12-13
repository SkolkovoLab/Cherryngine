package ru.cherryngine.lib.minecraft.r2

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.utils.toKey

class StaticRegistry<T : StaticProtocolObject>(
    override val key: Key,
    entries: List<T>,
    override val tags: Map<Key, List<Key>>,
) : Registry<T> {
    private val keyToValue = entries.associateBy { it.key() }
    private val valueToKey = entries.associateWith { RegistryKey.Impl<T>(it.key()) }
    private val idToValue = entries.toList()

    init {
        idToValue.forEachIndexed { index, value ->
            check(index == value.id)
        }
    }

    override fun getOrNull(id: Int): T? {
        return idToValue.getOrNull(id)
    }

    override fun getOrNull(key: Key): T? {
        return keyToValue[key]
    }

    override fun getKeyOrNull(id: Int): RegistryKey<T>? {
        val value = idToValue.getOrNull(id) ?: return null
        return RegistryKey.Impl(value.key())
    }

    override fun getKeyOrNull(value: T): RegistryKey<T>? {
        return valueToKey[value]
    }

    override fun getKeyOrNull(key: Key): RegistryKey<T>? {
        if (key !in keyToValue) return null
        return RegistryKey.Impl(key)
    }

    override fun getIdOrNull(key: Key): Int? {
        val value = keyToValue[key] ?: return null
        return value.id
    }

    override val size: Int
        get() = keyToValue.size
    override val keys: Collection<RegistryKey<T>>
        get() = valueToKey.values
    override val values: Collection<T>
        get() = valueToKey.keys

    override val keyCodec: Codec<T> = Codec.KEY.transform(
        { get(it) },
        { getKey(it).key() }
    )
    override val streamCodec = Registry2StreamCodec(this)

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        fun <T : StaticProtocolObject> create(
            key: Key,
            loader: (key: String, jsonObject: JsonObject) -> T,
            resourceName: String,
        ): StaticRegistry<T> {
            val resource = ClassLoader.getSystemResource(resourceName)
                ?: throw IllegalStateException("Resource $resourceName not found for registry $key")

            val map: Map<String, JsonObject> = resource.openStream().use {
                Json.decodeFromStream<Map<String, JsonObject>>(it)
            }

            val entries = map.map { (key, value) -> loader(key, value) }.sortedBy { it.id }

            val tags = createTags("tags/$resourceName")

            return StaticRegistry(key, entries, tags)
        }

        @OptIn(ExperimentalSerializationApi::class)
        fun createTags(resourceName: String): Map<Key, List<Key>> {
            val resource = ClassLoader.getSystemResource(resourceName) ?: return mapOf()

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
    }
}