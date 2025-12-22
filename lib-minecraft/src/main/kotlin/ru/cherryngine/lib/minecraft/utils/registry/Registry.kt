package ru.cherryngine.lib.minecraft.utils.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.network.stream_codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.registry.TagRegistry
import ru.cherryngine.lib.minecraft.utils.KeyedKt
import ru.cherryngine.lib.minecraft.utils.toKey

interface Registry<T : Any> : KeyedKt {
    fun getOrNull(value: T): RegistryEntry<T>?
    fun getOrNull(id: Int): RegistryEntry<T>?
    fun getOrNull(key: Key): RegistryEntry<T>?
    fun getOrNull(key: String): RegistryEntry<T>? = getOrNull(key.toKey())

    fun getOrNull(holder: RegistryEntryHolder<T>): RegistryEntry<T>? = when (holder) {
        is RegistryEntryHolder.Id -> getOrNull(holder.id)
        is RegistryEntryHolder.Key -> getOrNull(holder.key)
        is RegistryEntryHolder.Value -> getOrNull(holder.value)
    }

    operator fun get(value: T): RegistryEntry<T> = getOrNull(value)
        ?: throw NoSuchElementException("No entry found in registry '${key()}' for value=$value")

    operator fun get(id: Int): RegistryEntry<T> = getOrNull(id)
        ?: throw NoSuchElementException("No entry found in registry '${key()}' for id=$id")

    operator fun get(key: Key): RegistryEntry<T> = getOrNull(key)
        ?: throw NoSuchElementException("No entry found in registry '${key()}' for key=$key")

    operator fun get(key: String): RegistryEntry<T> = get(key.toKey())

    operator fun get(holder: RegistryEntryHolder<T>): RegistryEntry<T> = when (holder) {
        is RegistryEntryHolder.Id -> get(holder.id)
        is RegistryEntryHolder.Key -> get(holder.key)
        is RegistryEntryHolder.Value -> get(holder.value)
    }

    val entries: List<RegistryEntry<T>>
    val keys: Set<Key>
    val values: Set<T>
    val size: Int get() = entries.size

    val keyCodec: Codec<T>
    val streamCodec: RegistryStreamCodec<T>

    val tags: Map<Key, List<Key>>

    fun getTagRegistry(): TagRegistry {
        val tags = tags.entries.map { (key, value) ->
            TagRegistry.Tag(key, value.map { get(it).id })
        }
        return TagRegistry(key, tags)
    }
}
