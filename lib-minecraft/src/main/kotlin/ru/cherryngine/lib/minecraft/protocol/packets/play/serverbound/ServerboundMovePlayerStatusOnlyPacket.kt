package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundMovePlayerStatusOnlyPacket(
    val flags: MovePlayerFlags
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            MovePlayerFlags.STREAM_CODEC, ServerboundMovePlayerStatusOnlyPacket::flags,
            ::ServerboundMovePlayerStatusOnlyPacket
        )
    }
}