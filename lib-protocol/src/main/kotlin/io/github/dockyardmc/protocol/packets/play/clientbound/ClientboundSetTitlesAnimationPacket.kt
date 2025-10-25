package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundSetTitlesAnimationPacket(
    val fadeIn: Int,
    val stay: Int,
    val fadeOut: Int
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.INT, ClientboundSetTitlesAnimationPacket::fadeIn,
            StreamCodec.INT, ClientboundSetTitlesAnimationPacket::stay,
            StreamCodec.INT, ClientboundSetTitlesAnimationPacket::fadeOut,
            ::ClientboundSetTitlesAnimationPacket
        )
    }
}