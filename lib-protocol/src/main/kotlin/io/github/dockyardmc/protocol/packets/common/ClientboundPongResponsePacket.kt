package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundPongResponsePacket(
    val payload: Long
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.LONG, ClientboundPongResponsePacket::payload,
            ::ClientboundPongResponsePacket
        )
    }
}