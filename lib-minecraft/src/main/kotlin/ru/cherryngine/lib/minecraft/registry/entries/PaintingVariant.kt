package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.kotlinx.ComponentToJsonElementSerializer

@Serializable
data class PaintingVariant(
    val identifier: String,
    @SerialName("asset_id")
    val assetId: String,
    val height: Int,
    val width: Int,
    @Serializable(ComponentToJsonElementSerializer::class)
    val author: Component? = null,
    @Serializable(ComponentToJsonElementSerializer::class)
    val title: Component,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("asset_id", assetId)
            withInt("height", height)
            withInt("width", width)
        }
    }
}