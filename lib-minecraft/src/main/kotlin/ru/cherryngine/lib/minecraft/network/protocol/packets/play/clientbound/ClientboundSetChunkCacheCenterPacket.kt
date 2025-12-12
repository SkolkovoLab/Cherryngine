package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundSetChunkCacheCenterPacket(
    val chunkPos: ChunkPos,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ChunkPos.STREAM_CODEC_VAR_INT, ClientboundSetChunkCacheCenterPacket::chunkPos,
            ::ClientboundSetChunkCacheCenterPacket
        )
    }
}