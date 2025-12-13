package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.r2.PaintingVariant
import ru.cherryngine.lib.minecraft.r2.Registries

data class PaintingVariantComponent(
    val variant: PaintingVariant,
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Registries.paintingVariant.streamCodec, PaintingVariantComponent::variant,
            ::PaintingVariantComponent
        )
    }
}