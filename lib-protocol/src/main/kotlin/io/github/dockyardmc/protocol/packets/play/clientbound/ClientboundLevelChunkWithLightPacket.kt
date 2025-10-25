package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.ChunkPos
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.world.Light
import io.github.dockyardmc.world.chunk.ChunkData

data class ClientboundLevelChunkWithLightPacket(
    val chunkPos: ChunkPos,
    val chunkData: ChunkData,
    val light: Light
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ChunkPos.STREAM_CODEC_INT, ClientboundLevelChunkWithLightPacket::chunkPos,
            ChunkData.STREAM_CODEC, ClientboundLevelChunkWithLightPacket::chunkData,
            Light.STREAM_CODEC, ClientboundLevelChunkWithLightPacket::light,
            ::ClientboundLevelChunkWithLightPacket
        )
    }
}