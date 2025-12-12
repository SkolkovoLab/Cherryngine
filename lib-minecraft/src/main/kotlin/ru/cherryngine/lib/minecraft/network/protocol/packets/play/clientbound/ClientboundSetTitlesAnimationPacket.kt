package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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