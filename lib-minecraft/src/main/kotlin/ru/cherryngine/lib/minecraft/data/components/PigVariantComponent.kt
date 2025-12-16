package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.PigVariant

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