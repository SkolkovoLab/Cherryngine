package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundResetScorePacket(
    val name: String,
    val objective: String?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ClientboundResetScorePacket::name,
            StreamCodec.STRING.optional(), ClientboundResetScorePacket::objective,
            ::ClientboundResetScorePacket
        )
    }
}