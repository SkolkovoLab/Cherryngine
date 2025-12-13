package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.r2.PigVariant
import ru.cherryngine.lib.minecraft.r2.Registries

class PigVariantComponent(
    val variant: PigVariant,
) : DynamicVariantComponent<PigVariant>(variant, Registries.pigVariant) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Registries.pigVariant.streamCodec, PigVariantComponent::variant,
            ::PigVariantComponent
        )
    }
}