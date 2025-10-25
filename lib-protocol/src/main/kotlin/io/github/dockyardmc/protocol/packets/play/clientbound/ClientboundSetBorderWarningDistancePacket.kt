package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundSetBorderWarningDistancePacket(
    val distance: Int
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundSetBorderWarningDistancePacket::distance,
            ::ClientboundSetBorderWarningDistancePacket
        )
    }
}