package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundKeepAlivePacket(
    val keepAliveId: Long
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.LONG, ClientboundKeepAlivePacket::keepAliveId,
            ::ClientboundKeepAlivePacket
        )
    }
}