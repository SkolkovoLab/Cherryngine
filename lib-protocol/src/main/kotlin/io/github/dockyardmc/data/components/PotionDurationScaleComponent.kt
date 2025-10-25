package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.StreamCodec

class PotionDurationScaleComponent(
    val duration: Float
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofFloat(duration))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, PotionDurationScaleComponent::duration,
            ::PotionDurationScaleComponent
        )
    }
}