package ru.cherryngine.lib.minecraft.utils.registry

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.network.stream_codec.RegistryStreamCodec

class StaticRegistry<T : StaticProtocolObject>(
    override val key: Key,
    values: List<T>,
) : Registry<T> {
    init {
        values.forEachIndexed { index, value -> check(index == value.id) }
    }

    private val entriesById: List<RegistryEntry<T>> = values.map { RegistryEntry(it, it.key, it.id) }
    private val entriesByKey: Map<Key, RegistryEntry<T>> = entriesById.associateBy { it.key }
    private val entriesByValue: Map<T, RegistryEntry<T>> = entriesById.associateBy { it.value }

    override fun getOrNull(value: T): RegistryEntry<T>? {
        return entriesByValue[value]
    }

    override fun getOrNull(id: Int): RegistryEntry<T>? {
        return entriesById.getOrNull(id)
    }

    override fun getOrNull(key: Key): RegistryEntry<T>? {
        return entriesByKey[key]
    }

    override val entries: List<RegistryEntry<T>>
        get() = entriesById
    override val keys: Set<Key>
        get() = entriesByKey.keys
    override val values: Set<T>
        get() = entriesByValue.keys

    override val keyCodec: Codec<T> = Codec.KEY.transform(
        { get(it).value },
        { get(it).key }
    )
    override val streamCodec = RegistryStreamCodec(this)

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

            return StaticRegistry(key, entries)
        }
    }
}