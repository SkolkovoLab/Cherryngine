package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.BinaryTag
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object AttributeRegistry : DataDrivenRegistry<Attribute>() {
    override val identifier: String = "minecraft:attribute"
}

@Serializable
data class Attribute(
    val identifier: String,
    val translationKey: String,
    val defaultValue: Double,
    val clientSync: Boolean,
    val minValue: Double? = null,
    val maxValue: Double? = null
) : RegistryEntry {

    override fun getNbt(): BinaryTag? {
        return null
    }

    override fun getProtocolId(): Int {
        return AttributeRegistry.getProtocolIdByEntry(this)
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}