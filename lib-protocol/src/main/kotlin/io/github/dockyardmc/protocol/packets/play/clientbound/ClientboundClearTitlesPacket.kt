package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundClearTitlesPacket(
    val reset: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BOOLEAN, ClientboundClearTitlesPacket::reset,
            ::ClientboundClearTitlesPacket
        )
    }
}