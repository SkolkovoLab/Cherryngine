package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundClearTitlesPacket(
    val reset: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BOOLEAN, ClientboundClearTitlesPacket::reset,
            ::ClientboundClearTitlesPacket
        )
    }
}