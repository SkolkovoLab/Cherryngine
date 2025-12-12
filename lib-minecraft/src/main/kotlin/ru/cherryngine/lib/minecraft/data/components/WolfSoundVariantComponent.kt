package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.WolfSoundVariant
import ru.cherryngine.lib.minecraft.registry.registries.WolfSoundVariantRegistry

class WolfSoundVariantComponent(
    val variant: WolfSoundVariant,
) : DynamicVariantComponent<WolfSoundVariant>(variant, WolfSoundVariantRegistry) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            WolfSoundVariantRegistry.STREAM_CODEC, WolfSoundVariantComponent::variant,
            ::WolfSoundVariantComponent
        )
    }
}