package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.kotlinx.ComponentToJsonStringSerializer
import ru.cherryngine.lib.minecraft.utils.toNBT

@Serializable
data class TrimMaterial(
    val identifier: String,
    val assetName: String,
    @Serializable(ComponentToJsonStringSerializer::class)
    val description: Component,
    val overrideArmorMaterials: Map<String, String>? = null,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("asset_name", assetName)
            withCompound("description", description.toNBT())
        }
    }
}