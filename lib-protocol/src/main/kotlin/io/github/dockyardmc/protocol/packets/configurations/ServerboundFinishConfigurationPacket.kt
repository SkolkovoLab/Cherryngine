package io.github.dockyardmc.protocol.packets.configurations

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ServerboundFinishConfigurationPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ServerboundFinishConfigurationPacket
        )
    }
}