package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.protocol.types.DyeColor
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class WolfCollarComponent(
    val color: DyeColor
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<DyeColor>(), WolfCollarComponent::color,
            ::WolfCollarComponent
        )
    }
}