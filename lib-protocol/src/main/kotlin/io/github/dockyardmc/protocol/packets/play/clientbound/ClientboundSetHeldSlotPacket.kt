package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundSetHeldSlotPacket(
    val slot: Byte
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BYTE, ClientboundSetHeldSlotPacket::slot,
            ::ClientboundSetHeldSlotPacket
        )
    }
}