package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.protocol.types.ParrotVariant
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ParrotVariantComponent(
    val parrotColor: ParrotVariant
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<ParrotVariant>(), ParrotVariantComponent::parrotColor,
            ::ParrotVariantComponent
        )
    }
}