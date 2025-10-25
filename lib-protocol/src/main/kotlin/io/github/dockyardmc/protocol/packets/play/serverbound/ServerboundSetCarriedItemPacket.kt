package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundSetCarriedItemPacket(
    val slot: Int
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.INT_SHORT, ServerboundSetCarriedItemPacket::slot,
            ::ServerboundSetCarriedItemPacket
        )
    }
}