package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.kotlinx.ComponentToJsonStringSerializer
import ru.cherryngine.lib.minecraft.utils.toNBT

object TrimPatternRegistry : DataDrivenRegistry<TrimPattern>() {
    override val identifier: String = "minecraft:trim_pattern"
    val STREAM_CODEC = RegistryStreamCodec(this)
}

@Serializable
data class TrimPattern(
    val identifier: String,
    val assetId: String,
    val decal: Boolean,
    @Serializable(with = ComponentToJsonStringSerializer::class)
    val description: Component,
) : RegistryEntry {

    override fun getProtocolId(): Int {
        return TrimPatternRegistry.getProtocolIdByEntry(this)
    }

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