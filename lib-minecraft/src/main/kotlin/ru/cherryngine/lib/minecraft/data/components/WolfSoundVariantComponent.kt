package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.WolfSoundVariant

class WolfSoundVariantComponent(
    val variant: WolfSoundVariant,
) : DynamicVariantComponent<WolfSoundVariant>(variant, Registries.wolfSoundVariant) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Registries.wolfSoundVariant.streamCodec, WolfSoundVariantComponent::variant,
            ::WolfSoundVariantComponent
        )
    }
}