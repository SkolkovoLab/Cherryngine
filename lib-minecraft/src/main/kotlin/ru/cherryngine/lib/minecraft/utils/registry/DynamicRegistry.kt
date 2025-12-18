package ru.cherryngine.lib.minecraft.utils.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.network.stream_codec.RegistryStreamCodec

class DynamicRegistry<T : Any>(
    override val key: Key,
) : Registry<T> {
    private val idToValue: MutableList<T> = arrayListOf()
    private val idToKey: MutableList<RegistryKey<T>> = arrayListOf()
    private val keyToId: MutableMap<Key, Int> = hashMapOf()
    private val keyToValue: MutableMap<Key, T> = hashMapOf()
    private val valueToKey: MutableMap<T, RegistryKey<T>> = hashMapOf()

    override val tags: MutableMap<Key, MutableList<Key>> = hashMapOf()

    override fun getOrNull(id: Int): T? {
        return idToValue.getOrNull(id)
    }

    override fun getOrNull(key: Key): T? {
        return keyToValue[key]
    }

    override fun getKeyOrNull(id: Int): RegistryKey<T>? {
        return idToKey.getOrNull(id)
    }

    override fun getKeyOrNull(value: T): RegistryKey<T>? {
        return valueToKey[value]
    }

    override fun getKeyOrNull(key: Key): RegistryKey<T>? {
        if (key !in keyToValue) return null
        return RegistryKey(key)
    }

    override fun getIdOrNull(key: Key): Int? {
        return keyToId[key]
    }

    override val size: Int
        get() = idToValue.size
    override val keys: Collection<RegistryKey<T>>
        get() = idToKey
    override val values: Collection<T>
        get() = idToValue

    fun register(key: Key, value: T): RegistryKey<T> {
        val registryKey: RegistryKey<T> = RegistryKey(key)
        synchronized(REGISTRY_LOCK) {
            val id = keyToId[key] // Array set at home
            keyToValue[key] = value
            valueToKey[value] = registryKey
            if (id == null) {
                idToValue.add(value)
                idToKey.add(registryKey)
                keyToId[key] = idToValue.lastIndex
            } else {
                idToValue[id] = value
                idToKey[id] = registryKey
                keyToId[key] = id
            }

            return registryKey
        }
    }

    override val keyCodec: Codec<T> = Codec.KEY.transform(
        { get(it) },
        { getKey(it).key() }
    )
    override val streamCodec = RegistryStreamCodec(this)

    companion object {
        private val REGISTRY_LOCK: Any = Any()
    }
}