package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.light.LightData

data class ClientboundLightUpdatePacket(
    val chunkPos: ChunkPos,
    val lightData: LightData
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ChunkPos.STREAM_CODEC_VAR_INT, ClientboundLightUpdatePacket::chunkPos,
            LightData.STREAM_CODEC, ClientboundLightUpdatePacket::lightData,
            ::ClientboundLightUpdatePacket
        )
    }
}