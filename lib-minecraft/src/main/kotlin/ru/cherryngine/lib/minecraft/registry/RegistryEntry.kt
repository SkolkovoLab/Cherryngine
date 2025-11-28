package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.nbt.BinaryTag

interface RegistryEntry {
    fun getNbt(): BinaryTag? = null
    fun getEntryIdentifier(): String
}