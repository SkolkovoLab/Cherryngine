package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

class PaintingVariantRegistry : DataDrivenRegistry<PaintingVariant>() {
    override val identifier: String = "minecraft:painting_variant"
}

@Serializable
data class PaintingVariant(
    val identifier: String,
    val assetId: String,
    val height: Int,
    val width: Int,
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