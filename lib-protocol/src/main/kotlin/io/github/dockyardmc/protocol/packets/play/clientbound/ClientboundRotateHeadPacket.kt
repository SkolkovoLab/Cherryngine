package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundRotateHeadPacket(
    val entityId: Int,
    val yaw: Float
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundRotateHeadPacket::entityId,
            LocationCodecs.ANGLE, ClientboundRotateHeadPacket::yaw,
            ::ClientboundRotateHeadPacket
        )
    }
}