package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.StreamCodec

class TooltipStyleComponent(
    val style: String
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofString(style))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, TooltipStyleComponent::style,
            ::TooltipStyleComponent
        )
    }
}