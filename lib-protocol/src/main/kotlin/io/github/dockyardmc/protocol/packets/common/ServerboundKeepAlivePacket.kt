package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundKeepAlivePacket(
    val keepAliveId: Long
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.LONG, ServerboundKeepAlivePacket::keepAliveId,
            ::ServerboundKeepAlivePacket
        )
    }
}