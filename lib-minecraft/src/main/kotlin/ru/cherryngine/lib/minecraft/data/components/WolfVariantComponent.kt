package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.registry.registries.WolfVariant
import ru.cherryngine.lib.minecraft.registry.registries.WolfVariantRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class WolfVariantComponent(
    val variant: WolfVariant,
) : DynamicVariantComponent<WolfVariant>(variant, WolfVariantRegistry) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            WolfVariantRegistry.STREAM_CODEC, WolfVariantComponent::variant,
            ::WolfVariantComponent
        )
    }
}