package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

class FluidRegistry : DataDrivenRegistry<Fluid>() {
    override val identifier: String = "minecraft:fluid"
}

@Serializable
data class Fluid(
    val identifier: String,
    val dripParticle: String?,
    val pickupSound: String,
    val explosionResistance: Float,
    val block: String
) : RegistryEntry {
    override fun getNbt(): CompoundBinaryTag? = null

    override fun getEntryIdentifier(): String {
        return identifier
    }

}