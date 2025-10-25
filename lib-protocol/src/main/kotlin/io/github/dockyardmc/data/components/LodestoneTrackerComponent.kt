package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.protocol.types.WorldPosition
import io.github.dockyardmc.tide.stream.StreamCodec

class LodestoneTrackerComponent(
    val worldPosition: WorldPosition?,
    val tracked: Boolean
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            optionalStruct("target", worldPosition, WorldPosition::hashStruct)
            default("tracked", true, tracked, CRC32CHasher::ofBoolean)
        }
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            WorldPosition.STREAM_CODEC.optional(), LodestoneTrackerComponent::worldPosition,
            StreamCodec.BOOLEAN, LodestoneTrackerComponent::tracked,
            ::LodestoneTrackerComponent
        )
    }
}