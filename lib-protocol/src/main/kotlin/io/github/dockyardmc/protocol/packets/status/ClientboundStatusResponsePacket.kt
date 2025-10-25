package io.github.dockyardmc.protocol.packets.status

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundStatusResponsePacket(
    val statusJson: String
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ClientboundStatusResponsePacket::statusJson,
            ::ClientboundStatusResponsePacket
        )
    }
}