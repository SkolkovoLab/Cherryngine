package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.registry.registries.WolfVariant
import io.github.dockyardmc.registry.registries.WolfVariantRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

class WolfVariantComponent(
    val variant: WolfVariant
) : DynamicVariantComponent<WolfVariant>(variant, WolfVariantRegistry) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(WolfVariantRegistry), WolfVariantComponent::variant,
            ::WolfVariantComponent
        )
    }
}