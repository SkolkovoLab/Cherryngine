package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.BinaryTag
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class Attribute(
    val identifier: String,
    val translationKey: String,
    val defaultValue: Double,
    val clientSync: Boolean,
    val maxValue: Double,
    val minValue: Double,
) : RegistryEntry {

    override fun getNbt(): BinaryTag? {
        return null
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}