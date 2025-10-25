package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.registry.registries.WolfSoundVariant
import ru.cherryngine.lib.minecraft.registry.registries.WolfSoundVariantRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class WolfSoundVariantComponent(
    val variant: WolfSoundVariant
) : DynamicVariantComponent<WolfSoundVariant>(variant, WolfSoundVariantRegistry) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(WolfSoundVariantRegistry), WolfSoundVariantComponent::variant,
            ::WolfSoundVariantComponent
        )
    }
}