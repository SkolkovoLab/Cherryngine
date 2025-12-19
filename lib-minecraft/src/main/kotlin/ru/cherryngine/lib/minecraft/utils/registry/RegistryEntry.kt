package ru.cherryngine.lib.minecraft.utils.registry

import net.kyori.adventure.key.Key

data class RegistryEntry<T : Any>(
    val value: T,
    val key: Key,
    val id: Int,
)