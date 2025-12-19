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

    operator fun get(value: T): RegistryEntry<T> = getOrNull(value)
        ?: throw NoSuchElementException("No entry found in registry '${key()}' for value=$value")

    operator fun get(id: Int): RegistryEntry<T> = getOrNull(id)
        ?: throw NoSuchElementException("No entry found in registry '${key()}' for id=$id")

    operator fun get(key: Key): RegistryEntry<T> = getOrNull(key)
        ?: throw NoSuchElementException("No entry found in registry '${key()}' for key=$key")

    operator fun get(key: String): RegistryEntry<T> = getOrNull(key)
        ?: throw NoSuchElementException("No entry found in registry '${key()}' for key=$key")

    fun getOrNull(holder: RegistryEntryHolder<T>): RegistryEntry<T>? = when (holder) {
        is RegistryEntryHolder.Id -> getOrNull(holder.id)
        is RegistryEntryHolder.Key -> getOrNull(holder.key)
        is RegistryEntryHolder.Value -> getOrNull(holder.value)
    }

    operator fun get(holder: RegistryEntryHolder<T>): RegistryEntry<T> = when (holder) {
        is RegistryEntryHolder.Id -> get(holder.id)
        is RegistryEntryHolder.Key -> get(holder.key)
        is RegistryEntryHolder.Value -> get(holder.value)
    }

    fun getValueOrNull(id: Int): T? = getOrNull(id)?.value
    fun getValueOrNull(key: Key): T? = getOrNull(key)?.value
    fun getValueOrNull(key: String): T? = getOrNull(key)?.value

    fun getValue(id: Int): T = get(id).value
    fun getValue(key: Key): T = get(key).value
    fun getValue(key: String): T = get(key).value

    fun getKeyOrNull(id: Int): Key? = getOrNull(id)?.key
    fun getKeyOrNull(value: T): Key? = getOrNull(value)?.key

    fun getKey(id: Int): Key = get(id).key
    fun getKey(value: T): Key = get(value).key

    fun getIdOrNull(key: Key): Int? = getOrNull(key)?.id
    fun getIdOrNull(key: String): Int? = getOrNull(key)?.id
    fun getIdOrNull(value: T): Int? = getOrNull(value)?.id

    fun getId(key: Key): Int = get(key).id
    fun getId(key: String): Int = get(key).id
    fun getId(value: T): Int = get(value).id

    val size: Int
    val keys: Collection<Key>
    val values: Collection<T>

    val keyCodec: Codec<T>
    val streamCodec: RegistryStreamCodec<T>

    val tags: Map<Key, List<Key>>

    fun getTagRegistry(): TagRegistry {
        val tags = tags.entries.map { (key, value) ->
            TagRegistry.Tag(key, value.map { getId(it) })
        }
        return TagRegistry(key, tags)
    }
}
