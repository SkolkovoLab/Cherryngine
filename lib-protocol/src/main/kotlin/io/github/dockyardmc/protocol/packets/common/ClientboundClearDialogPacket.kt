package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ClientboundClearDialogPacket : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(::ClientboundClearDialogPacket)
    }
}