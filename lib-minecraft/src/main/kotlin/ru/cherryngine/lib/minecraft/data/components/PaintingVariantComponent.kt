package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.PaintingVariant
import ru.cherryngine.lib.minecraft.registry.registries.PaintingVariantRegistry

data class PaintingVariantComponent(
    val variant: PaintingVariant,
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            PaintingVariantRegistry.STREAM_CODEC, PaintingVariantComponent::variant,
            ::PaintingVariantComponent
        )
    }
}