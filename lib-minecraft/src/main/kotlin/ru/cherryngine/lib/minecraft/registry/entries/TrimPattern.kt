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
data class TrimPattern(
    val identifier: String,
    @SerialName("asset_id")
    val assetId: String,
    val decal: Boolean,
    @Serializable(with = ComponentToJsonElementSerializer::class)
    val description: Component,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("asset_id", assetId)
            withBoolean("decal", decal)
            withCompound("description", description.toNBT())
        }
    }
}