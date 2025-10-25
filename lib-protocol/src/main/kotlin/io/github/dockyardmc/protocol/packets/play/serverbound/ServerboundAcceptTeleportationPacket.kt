package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundAcceptTeleportationPacket(
    val teleportId: Int
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundAcceptTeleportationPacket::teleportId,
            ::ServerboundAcceptTeleportationPacket
        )
    }
}