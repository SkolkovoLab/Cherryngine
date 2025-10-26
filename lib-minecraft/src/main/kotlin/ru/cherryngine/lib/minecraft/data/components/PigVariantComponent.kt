package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.registry.registries.PigVariant
import ru.cherryngine.lib.minecraft.registry.registries.PigVariantRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class PigVariantComponent(
    val variant: PigVariant,
) : DynamicVariantComponent<PigVariant>(variant, PigVariantRegistry) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            PigVariantRegistry.STREAM_CODEC, PigVariantComponent::variant,
            ::PigVariantComponent
        )
    }
}