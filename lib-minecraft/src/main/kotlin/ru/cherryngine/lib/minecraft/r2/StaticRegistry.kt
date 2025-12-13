package ru.cherryngine.lib.minecraft.r2

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import net.kyori.adventure.key.Key

class StaticRegistry<T : StaticProtocolObject>(
    val key: Key,
    entries: List<T>,
) : Registry<T> {
    private val keyToValue = entries.associateBy { it.key() }
    private val valueToKey = entries.associateWith { RegistryKey.Impl<T>(it.key()) }
    private val idToValue = entries.toList()

    init {
        idToValue.forEachIndexed { index, value ->
            check(index == value.id)
        }
    }

    override fun key(): Key = key

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

    override fun getIdOrNull(key: RegistryKey<T>): Int? {
        val value = keyToValue[key.key()] ?: return null
        return value.id
    }

    override val size: Int
        get() = keyToValue.size
    override val keys: Collection<RegistryKey<T>>
        get() = valueToKey.values
    override val values: Collection<T>
        get() = valueToKey.keys

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        fun <T : StaticProtocolObject> create(
            key: Key,
            loader: (key: String, jsonObject: JsonObject) -> T,
            resource: String,
        ): StaticRegistry<T> {
            val resource = ClassLoader.getSystemResource(resource)
                ?: throw IllegalStateException("Resource $resource not found for registry $key")

            val map: Map<String, JsonObject> = resource.openStream().use {
                Json.decodeFromStream<Map<String, JsonObject>>(it)
            }

            val entries = map.map { (key, value) -> loader(key, value) }.sortedBy { it.id }

            return StaticRegistry(key, entries)
        }
    }
}