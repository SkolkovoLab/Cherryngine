package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object FrogVariantRegistry : DataDrivenRegistry<FrogVariant>() {
    override val identifier: String = "minecraft:frog_variant"
    val STREAM_CODEC = RegistryStreamCodec(this)
}

@Serializable
data class FrogVariant(
    val identifier: String,
    val assetId: String,
) : RegistryEntry {

    override fun getProtocolId(): Int {
        return FrogVariantRegistry.getProtocolIdByEntry(this)
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("asset_id", assetId)
        }
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }

}