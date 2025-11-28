package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class Fluid(
    val identifier: String,
    val dripParticle: String?,
    val pickupSound: String,
    val explosionResistance: Float,
    val block: String,
) : RegistryEntry {
    override fun getNbt(): CompoundBinaryTag? = null

    override fun getEntryIdentifier(): String {
        return identifier
    }
}