package io.github.dockyardmc.protocol.packets.login

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf

data class ClientboundLoginCustomQueryPacket(
    val messageId: Int,
    val channel: String,
    val data: ByteBuf
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundLoginCustomQueryPacket::messageId,
            StreamCodec.STRING, ClientboundLoginCustomQueryPacket::channel,
            StreamCodec.RAW_BYTES, ClientboundLoginCustomQueryPacket::data,
            ::ClientboundLoginCustomQueryPacket
        )
    }
}