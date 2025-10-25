package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ServerboundClientTickEndPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ServerboundClientTickEndPacket
        )
    }
}