package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.tide.stream.StreamCodec

class DamageResistantComponent(
    val tagKey: String
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            static("types", CRC32CHasher.ofString(tagKey))
        }
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, DamageResistantComponent::tagKey,
            ::DamageResistantComponent
        )
    }
}