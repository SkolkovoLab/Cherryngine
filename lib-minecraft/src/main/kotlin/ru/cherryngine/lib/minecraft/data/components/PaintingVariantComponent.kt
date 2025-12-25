package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.PaintingVariant

data class PaintingVariantComponent(
    val variant: PaintingVariant,
) : DataComponent() {
    companion object {
        val CODEC = Registries.paintingVariant.keyCodec.transform(
            ::PaintingVariantComponent,
            PaintingVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            Registries.paintingVariant.streamCodec, PaintingVariantComponent::variant,
            ::PaintingVariantComponent
        )
    }
}