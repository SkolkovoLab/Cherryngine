package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.Difficulty
import ru.cherryngine.lib.minecraft.network.stream_codec.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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