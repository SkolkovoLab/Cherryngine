package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class LlamaVariantComponent(
    val variant: Variant
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Variant>(), LlamaVariantComponent::variant,
            ::LlamaVariantComponent
        )
    }

    enum class Variant {
        CREAMY,
        WHITE,
        BROWN,
        GAY
    }
}