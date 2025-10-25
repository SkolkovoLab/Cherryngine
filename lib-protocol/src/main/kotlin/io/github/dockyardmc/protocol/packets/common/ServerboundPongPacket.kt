package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundPongPacket(
    val id: Int
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.INT, ServerboundPongPacket::id,
            ::ServerboundPongPacket
        )
    }
}