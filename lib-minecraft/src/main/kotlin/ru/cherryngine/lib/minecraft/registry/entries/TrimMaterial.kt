package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.kotlinx.ComponentToJsonElementSerializer
import ru.cherryngine.lib.minecraft.utils.toNBT

@Serializable
data class TrimMaterial(
    val identifier: String,
    @SerialName("asset_name")
    val assetName: String,
    @Serializable(ComponentToJsonElementSerializer::class)
    val description: Component,
    @SerialName("override_armor_assets")
    val overrideArmorAssets: Map<String, String>? = null,
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