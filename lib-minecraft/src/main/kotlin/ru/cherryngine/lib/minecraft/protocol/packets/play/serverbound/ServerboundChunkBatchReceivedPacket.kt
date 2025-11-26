package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ServerboundChunkBatchReceivedPacket(
    val chunksPerTick: Float
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, ServerboundChunkBatchReceivedPacket::chunksPerTick,
            ::ServerboundChunkBatchReceivedPacket
        )
    }
}