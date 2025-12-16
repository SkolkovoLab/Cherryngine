package ru.cherryngine.lib.minecraft.utils.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.utils.KeyedKt

@Suppress("unused")
data class RegistryKey<T>(
    override val key: Key,
) : KeyedKt {
    override fun toString() = key.toString()
}