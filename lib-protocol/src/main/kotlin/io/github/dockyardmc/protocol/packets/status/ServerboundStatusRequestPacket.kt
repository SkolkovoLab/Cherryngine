package io.github.dockyardmc.protocol.packets.status

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ServerboundStatusRequestPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(::ServerboundStatusRequestPacket)
    }
}