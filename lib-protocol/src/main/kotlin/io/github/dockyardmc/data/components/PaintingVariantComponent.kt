package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.registry.registries.PaintingVariant
import io.github.dockyardmc.registry.registries.PaintingVariantRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

data class PaintingVariantComponent(
    val variant: PaintingVariant
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(PaintingVariantRegistry), PaintingVariantComponent::variant,
            ::PaintingVariantComponent
        )
    }
}