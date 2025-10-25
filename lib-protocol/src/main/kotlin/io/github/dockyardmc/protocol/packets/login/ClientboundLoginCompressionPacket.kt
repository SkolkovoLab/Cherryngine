package io.github.dockyardmc.protocol.packets.login

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundLoginCompressionPacket(
    val compression: Int
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundLoginCompressionPacket::compression,
            ::ClientboundLoginCompressionPacket
        )
    }
}