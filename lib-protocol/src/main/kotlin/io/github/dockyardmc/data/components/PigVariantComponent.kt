package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.registry.registries.PigVariant
import io.github.dockyardmc.registry.registries.PigVariantRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

class PigVariantComponent(
    val variant: PigVariant
) : DynamicVariantComponent<PigVariant>(variant, PigVariantRegistry) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(PigVariantRegistry), PigVariantComponent::variant,
            ::PigVariantComponent
        )
    }
}