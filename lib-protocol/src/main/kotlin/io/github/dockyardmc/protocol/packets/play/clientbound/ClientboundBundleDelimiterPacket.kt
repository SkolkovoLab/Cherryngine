package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ClientboundBundleDelimiterPacket : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ClientboundBundleDelimiterPacket
        )
    }
}
