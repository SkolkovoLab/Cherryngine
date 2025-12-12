package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.ChickenVariant
import ru.cherryngine.lib.minecraft.registry.registries.ChickenVariantRegistry

class ChickenVariantComponent(
    val variant: ChickenVariant,
) : DynamicVariantComponent<ChickenVariant>(variant, ChickenVariantRegistry) {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofRegistryEntry(variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ChickenVariantRegistry.STREAM_CODEC, ChickenVariantComponent::variant,
            ::ChickenVariantComponent
        )
    }
}