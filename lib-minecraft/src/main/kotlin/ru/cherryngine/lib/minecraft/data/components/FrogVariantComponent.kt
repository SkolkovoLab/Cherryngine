package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.FrogVariant
import ru.cherryngine.lib.minecraft.registry.registries.FrogVariantRegistry

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