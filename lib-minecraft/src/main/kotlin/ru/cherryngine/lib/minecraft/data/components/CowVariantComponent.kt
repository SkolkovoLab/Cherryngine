package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.registry.registries.CowVariant
import ru.cherryngine.lib.minecraft.registry.registries.CowVariantRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class CowVariantComponent(
    val variant: CowVariant,
) : DynamicVariantComponent<CowVariant>(variant, CowVariantRegistry) {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofRegistryEntry(variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            CowVariantRegistry.STREAM_CODEC, CowVariantComponent::variant,
            ::CowVariantComponent
        )
    }
}