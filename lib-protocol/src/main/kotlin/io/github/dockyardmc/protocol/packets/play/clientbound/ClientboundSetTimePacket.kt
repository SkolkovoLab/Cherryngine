package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundSetTimePacket(
    val worldAge: Long,
    val time: Long,
    val isFrozen: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.LONG, ClientboundSetTimePacket::worldAge,
            StreamCodec.LONG, ClientboundSetTimePacket::time,
            StreamCodec.BOOLEAN, ClientboundSetTimePacket::isFrozen,
            ::ClientboundSetTimePacket
        )
    }
}