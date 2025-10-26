package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.registry.registries.FrogVariant
import ru.cherryngine.lib.minecraft.registry.registries.FrogVariantRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class FrogVariantComponent(
    val variant: FrogVariant,
) : DynamicVariantComponent<FrogVariant>(variant, FrogVariantRegistry) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            FrogVariantRegistry.STREAM_CODEC, FrogVariantComponent::variant,
            ::FrogVariantComponent
        )
    }
}