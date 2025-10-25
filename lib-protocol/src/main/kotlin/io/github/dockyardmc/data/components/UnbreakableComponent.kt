package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.StreamCodec

class UnbreakableComponent : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.EMPTY_MAP)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(::UnbreakableComponent)
    }
}