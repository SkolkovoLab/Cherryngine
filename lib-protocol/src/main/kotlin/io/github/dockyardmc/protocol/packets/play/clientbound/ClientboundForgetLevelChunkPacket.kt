package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.ChunkPos
import io.github.dockyardmc.tide.stream.StreamCodec

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