package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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