package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object PotionTypeRegistry : DataDrivenRegistry<PotionType>() {
    override val identifier: String = "minecraft:potion"
    val STREAM_CODEC = RegistryStreamCodec(this)
}

@Serializable
data class PotionType(
    val identifier: String,
) : RegistryEntry {

    override fun getProtocolId(): Int {
        return PotionTypeRegistry.getProtocolIdByEntry(this)
    }

    override fun getNbt(): BinaryTag {
        return CompoundBinaryTag.empty()
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}