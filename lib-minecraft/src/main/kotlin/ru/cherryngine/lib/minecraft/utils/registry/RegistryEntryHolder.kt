package ru.cherryngine.lib.minecraft.utils.registry

import ru.cherryngine.lib.minecraft.utils.KeyedKt
import ru.cherryngine.lib.minecraft.utils.toKey
import net.kyori.adventure.key.Key as AdventureKey

sealed interface RegistryEntryHolder<T : Any> {
    companion object {
        fun <T : Any> fromId(id: Int) = Id<T>(id)
        fun <T : Any> fromKey(key: AdventureKey) = Key<T>(key)
        fun <T : Any> fromKey(key: String) = Key<T>(key.toKey())
        fun <T : Any> fromValue(value: T) = Value<T>(value)
    }

    data class Id<T : Any>(
        val id: Int,
    ) : RegistryEntryHolder<T>

    data class Key<T : Any>(
        override val key: AdventureKey,
    ) : RegistryEntryHolder<T>, KeyedKt

    data class Value<T : Any>(
        val value: T,
    ) : RegistryEntryHolder<T>
}

