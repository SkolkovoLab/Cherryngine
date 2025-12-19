package ru.cherryngine.lib.minecraft.utils.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.network.stream_codec.RegistryStreamCodec

class DynamicRegistry<T : Any>(
    override val key: Key,
) : Registry<T> {
    private val entries: MutableList<RegistryEntry<T>> = arrayListOf()
    private val entriesByKey: MutableMap<Key, RegistryEntry<T>> = hashMapOf()
    private val entriesByValue: MutableMap<T, RegistryEntry<T>> = hashMapOf()

    override val tags: MutableMap<Key, MutableList<Key>> = hashMapOf()

    override fun getOrNull(value: T): RegistryEntry<T>? {
        return entriesByValue[value]
    }

    override fun getOrNull(id: Int): RegistryEntry<T>? {
        return entries.getOrNull(id)
    }

    override fun getOrNull(key: Key): RegistryEntry<T>? {
        return entriesByKey[key]
    }

    override val size: Int
        get() = entries.size
    override val keys: Collection<Key>
        get() = entriesByKey.keys
    override val values: Collection<T>
        get() = entriesByValue.keys

    fun register(key: Key, value: T) {
        synchronized(REGISTRY_LOCK) {
            val current = entriesByKey[key]
            val id = current?.id ?: entries.size
            val entry = RegistryEntry(value, key, id)

            if (current != null) {
                entries[current.id] = (entry)
                entriesByValue.remove(current.value)
            } else {
                entries.add(entry)
            }
            entriesByKey[key] = entry
            entriesByValue[value] = entry
        }
    }

    override val keyCodec: Codec<T> = Codec.KEY.transform(
        { getValue(it) },
        { getKey(it) }
    )
    override val streamCodec = RegistryStreamCodec(this)

    companion object {
        private val REGISTRY_LOCK: Any = Any()
    }
}