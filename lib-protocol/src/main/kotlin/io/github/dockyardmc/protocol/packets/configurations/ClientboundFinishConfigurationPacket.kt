package io.github.dockyardmc.protocol.packets.configurations

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ClientboundFinishConfigurationPacket : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(::ClientboundFinishConfigurationPacket)
    }
}