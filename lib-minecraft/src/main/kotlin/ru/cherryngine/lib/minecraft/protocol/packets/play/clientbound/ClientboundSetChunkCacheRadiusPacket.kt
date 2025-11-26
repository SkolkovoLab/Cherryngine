package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSetChunkCacheRadiusPacket(
    val viewDistance: Int,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundSetChunkCacheRadiusPacket::viewDistance,
            ::ClientboundSetChunkCacheRadiusPacket
        )
    }
}