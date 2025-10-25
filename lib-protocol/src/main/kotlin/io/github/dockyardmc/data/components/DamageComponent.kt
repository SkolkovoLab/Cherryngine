package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.StreamCodec

class DamageComponent(
    val damage: Int
) : DataComponent(true) {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofInt(damage))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, DamageComponent::damage,
            ::DamageComponent
        )
    }
}