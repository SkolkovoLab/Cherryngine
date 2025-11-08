package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

class PotionTypeRegistry : DataDrivenRegistry<PotionType>() {
    override val identifier: String = "minecraft:potion"
}

@Serializable
data class PotionType(
    val identifier: String,
) : RegistryEntry {
    override fun getNbt(): BinaryTag {
        return CompoundBinaryTag.empty()
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}