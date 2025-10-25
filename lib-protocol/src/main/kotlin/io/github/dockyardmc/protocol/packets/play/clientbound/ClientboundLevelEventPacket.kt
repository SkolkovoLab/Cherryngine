package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.cherry.math.Vec3I
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.LevelEvent
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundLevelEventPacket(
    val event: LevelEvent,
    val location: Vec3I,
    val extraData: Int,
    val disableRelativeVolume: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LevelEvent.STREAM_CODEC, ClientboundLevelEventPacket::event,
            LocationCodecs.BLOCK_POSITION, ClientboundLevelEventPacket::location,
            StreamCodec.INT, ClientboundLevelEventPacket::extraData,
            StreamCodec.BOOLEAN, ClientboundLevelEventPacket::disableRelativeVolume,
            ::ClientboundLevelEventPacket
        )
    }
}