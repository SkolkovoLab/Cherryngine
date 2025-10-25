package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

class MooshroomVariantComponent(
    val variant: Variant
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Variant>(), MooshroomVariantComponent::variant,
            ::MooshroomVariantComponent
        )
    }

    enum class Variant {
        RED,
        BROWN
    }
}