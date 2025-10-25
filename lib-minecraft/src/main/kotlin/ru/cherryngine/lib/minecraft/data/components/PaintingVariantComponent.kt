package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.registry.registries.PaintingVariant
import ru.cherryngine.lib.minecraft.registry.registries.PaintingVariantRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class PaintingVariantComponent(
    val variant: PaintingVariant
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(PaintingVariantRegistry), PaintingVariantComponent::variant,
            ::PaintingVariantComponent
        )
    }
}