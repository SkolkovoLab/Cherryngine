package ru.cherryngine.lib.minecraft.registry

import ru.cherryngine.lib.minecraft.utils.registry.RegistryKey
import ru.cherryngine.lib.minecraft.utils.toKey

fun <T> createKey(key: String): RegistryKey<T> = RegistryKey(key.toKey())