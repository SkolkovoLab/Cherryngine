package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.registry.registries.FrogVariant
import io.github.dockyardmc.registry.registries.FrogVariantRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

class FrogVariantComponent(
    val variant: FrogVariant
) : DynamicVariantComponent<FrogVariant>(variant, FrogVariantRegistry) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(FrogVariantRegistry), FrogVariantComponent::variant,
            ::FrogVariantComponent
        )
    }
}