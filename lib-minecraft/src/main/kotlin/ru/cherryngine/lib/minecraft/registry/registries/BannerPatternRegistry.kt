package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object BannerPatternRegistry : DataDrivenRegistry<BannerPattern>() {
    override val identifier: String = "minecraft:banner_pattern"
    val STREAM_CODEC = RegistryStreamCodec(this)
}

@Serializable
data class BannerPattern(
    val identifier: String,
    val translationKey: String,
) : RegistryEntry {

    override fun getProtocolId(): Int {
        return BannerPatternRegistry.getProtocolIdByEntry(this)
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("asset_id", identifier)
            withString("translation_key", translationKey)
        }
    }
}
