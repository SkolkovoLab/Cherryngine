package io.github.dockyardmc.protocol.packets.login

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ServerboundLoginAcknowledgedPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ServerboundLoginAcknowledgedPacket
        )
    }
}