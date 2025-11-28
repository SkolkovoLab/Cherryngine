package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class PotionType(
    val identifier: String,
) : RegistryEntry {
    override fun getNbt(): BinaryTag {
        return CompoundBinaryTag.empty()
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}