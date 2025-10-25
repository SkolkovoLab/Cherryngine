package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object CatVariantRegistry : DataDrivenRegistry<CatVariant>() {
    override val identifier: String = "minecraft:cat_variant"
}

@Serializable
data class CatVariant(
    val identifier: String,
    val assetId: String,
) : RegistryEntry {

    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getProtocolId(): Int {
        return CatVariantRegistry.getProtocolIdByEntry(this)
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("asset_id", assetId)
        }
    }
}