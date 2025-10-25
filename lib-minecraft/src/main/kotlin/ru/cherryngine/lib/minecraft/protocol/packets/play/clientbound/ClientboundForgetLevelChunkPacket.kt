package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundForgetLevelChunkPacket(
    val chunkPos: ChunkPos,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ChunkPos.STREAM_CODEC_INT_SWAPPED, ClientboundForgetLevelChunkPacket::chunkPos,
            ::ClientboundForgetLevelChunkPacket
        )
    }
}