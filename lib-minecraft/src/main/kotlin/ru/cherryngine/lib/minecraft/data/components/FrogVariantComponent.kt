package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.r2.FrogVariant
import ru.cherryngine.lib.minecraft.r2.Registries

class FrogVariantComponent(
    val variant: FrogVariant,
) : DynamicVariantComponent<FrogVariant>(variant, Registries.frogVariant) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Registries.frogVariant.streamCodec, FrogVariantComponent::variant,
            ::FrogVariantComponent
        )
    }
}