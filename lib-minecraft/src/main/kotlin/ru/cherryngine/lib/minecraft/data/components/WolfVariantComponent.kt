package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.WolfVariant
import ru.cherryngine.lib.minecraft.registry.registries.WolfVariantRegistry

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