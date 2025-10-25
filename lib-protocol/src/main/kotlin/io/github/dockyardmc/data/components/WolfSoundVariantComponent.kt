package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.registry.registries.WolfSoundVariant
import io.github.dockyardmc.registry.registries.WolfSoundVariantRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

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