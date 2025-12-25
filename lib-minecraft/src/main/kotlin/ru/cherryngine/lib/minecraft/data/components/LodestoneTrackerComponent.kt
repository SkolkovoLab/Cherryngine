package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.protocol.types.WorldPosition
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class LodestoneTrackerComponent(
    val worldPosition: WorldPosition?,
    val tracked: Boolean
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            "target", WorldPosition.CODEC.optional(), LodestoneTrackerComponent::worldPosition,
            "tracked", Codec.BOOLEAN.default(true), LodestoneTrackerComponent::tracked,
            ::LodestoneTrackerComponent
        )
        val STREAM_CODEC = StreamCodec.of(
            WorldPosition.STREAM_CODEC.optional(), LodestoneTrackerComponent::worldPosition,
            StreamCodec.BOOLEAN, LodestoneTrackerComponent::tracked,
            ::LodestoneTrackerComponent
        )
    }
}