package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

class BannerPatternRegistry : DataDrivenRegistry<BannerPattern>() {
    override val identifier: String = "minecraft:banner_pattern"
}

@Serializable
data class BannerPattern(
    val identifier: String,
    val translationKey: String,
) : RegistryEntry {
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
