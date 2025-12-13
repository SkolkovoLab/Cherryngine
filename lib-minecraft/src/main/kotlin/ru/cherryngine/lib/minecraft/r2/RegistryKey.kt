package ru.cherryngine.lib.minecraft.r2

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.utils.KeyedKt

interface RegistryKey<T> : KeyedKt {
    data class Impl<T>(
        override val key: Key,
    ) : RegistryKey<T>
}