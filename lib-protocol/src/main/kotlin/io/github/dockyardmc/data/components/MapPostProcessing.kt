package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

class MapPostProcessing(
    val type: Type
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofEnum(type))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Type>(), MapPostProcessing::type,
            ::MapPostProcessing
        )
    }

    enum class Type {
        LOCK,
        SCALE
    }
}