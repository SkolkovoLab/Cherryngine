package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.protocol.types.WorldPosition
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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