package ru.cherryngine.lib.minecraft.r2

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed

interface RegistryKey<T> : Keyed {
    data class Impl<T>(
        val key: Key,
    ) : RegistryKey<T> {
        override fun key(): Key = key
    }
}