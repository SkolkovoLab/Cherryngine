package ru.cherryngine.lib.minecraft.registry.entries

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

data class DialogActionType(
    val identifier: String,
) : RegistryEntry {
    override fun getNbt(): CompoundBinaryTag? {
        return null
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}