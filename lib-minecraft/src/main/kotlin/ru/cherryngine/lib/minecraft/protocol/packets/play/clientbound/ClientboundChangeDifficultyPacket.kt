package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.Difficulty
import ru.cherryngine.lib.minecraft.tide.stream.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundChangeDifficultyPacket(
    val difficulty: Difficulty,
    val locked: Boolean = false
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ByteEnumStreamCodec<Difficulty>(), ClientboundChangeDifficultyPacket::difficulty,
            StreamCodec.BOOLEAN, ClientboundChangeDifficultyPacket::locked,
            ::ClientboundChangeDifficultyPacket
        )
    }
}