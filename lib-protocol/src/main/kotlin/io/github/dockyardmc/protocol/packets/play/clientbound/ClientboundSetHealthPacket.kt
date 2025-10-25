package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundSetHealthPacket(
    val health: Float,
    val food: Int,
    val saturation: Float
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, ClientboundSetHealthPacket::health,
            StreamCodec.VAR_INT, ClientboundSetHealthPacket::food,
            StreamCodec.FLOAT, ClientboundSetHealthPacket::saturation,
            ::ClientboundSetHealthPacket
        )
    }
}