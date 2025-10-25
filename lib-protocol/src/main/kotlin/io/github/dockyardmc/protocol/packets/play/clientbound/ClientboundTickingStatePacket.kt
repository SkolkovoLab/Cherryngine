package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundTickingStatePacket(
    val tickRate: Float,
    val isFrozen: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, ClientboundTickingStatePacket::tickRate,
            StreamCodec.BOOLEAN, ClientboundTickingStatePacket::isFrozen,
            ::ClientboundTickingStatePacket
        )
    }
}