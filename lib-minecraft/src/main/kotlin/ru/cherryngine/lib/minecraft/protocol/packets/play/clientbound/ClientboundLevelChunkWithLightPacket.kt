package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.light.LightData

data class ClientboundLevelChunkWithLightPacket(
    val chunkPos: ChunkPos,
    val chunkData: ChunkData,
    val lightData: LightData
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ChunkPos.STREAM_CODEC_INT, ClientboundLevelChunkWithLightPacket::chunkPos,
            ChunkData.STREAM_CODEC, ClientboundLevelChunkWithLightPacket::chunkData,
            LightData.STREAM_CODEC, ClientboundLevelChunkWithLightPacket::lightData,
            ::ClientboundLevelChunkWithLightPacket
        )
    }
}