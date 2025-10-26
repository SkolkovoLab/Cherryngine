package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.registry.registries.CatVariant
import ru.cherryngine.lib.minecraft.registry.registries.CatVariantRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class CatVariantComponent(
    val variant: CatVariant,
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofRegistryEntry(variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            CatVariantRegistry.STREAM_CODEC, CatVariantComponent::variant,
            ::CatVariantComponent
        )
    }
}