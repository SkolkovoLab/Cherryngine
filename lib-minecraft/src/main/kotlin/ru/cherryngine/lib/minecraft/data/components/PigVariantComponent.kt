package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.PigVariant
import ru.cherryngine.lib.minecraft.registry.registries.PigVariantRegistry

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