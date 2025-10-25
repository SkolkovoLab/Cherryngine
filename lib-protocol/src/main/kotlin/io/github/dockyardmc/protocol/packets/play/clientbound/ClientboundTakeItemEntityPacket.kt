package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundTakeItemEntityPacket(
    val collected: Int,
    val collector: Int,
    val amount: Int
) : ClientboundPacket {

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundTakeItemEntityPacket::collected,
            StreamCodec.VAR_INT, ClientboundTakeItemEntityPacket::collector,
            StreamCodec.VAR_INT, ClientboundTakeItemEntityPacket::amount,
            ::ClientboundTakeItemEntityPacket
        )
    }
}