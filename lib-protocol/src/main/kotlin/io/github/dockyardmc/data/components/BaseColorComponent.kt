package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.protocol.types.DyeColor
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class BaseColorComponent(
    val color: DyeColor
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofEnum(color))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<DyeColor>(), BaseColorComponent::color,
            ::BaseColorComponent
        )
    }
}