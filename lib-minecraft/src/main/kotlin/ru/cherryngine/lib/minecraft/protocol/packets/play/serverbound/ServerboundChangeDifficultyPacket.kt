package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.Difficulty
import ru.cherryngine.lib.minecraft.tide.stream.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundChangeDifficultyPacket(
    val difficulty: Difficulty,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ByteEnumStreamCodec<Difficulty>(), ServerboundChangeDifficultyPacket::difficulty,
            ::ServerboundChangeDifficultyPacket
        )
    }
}