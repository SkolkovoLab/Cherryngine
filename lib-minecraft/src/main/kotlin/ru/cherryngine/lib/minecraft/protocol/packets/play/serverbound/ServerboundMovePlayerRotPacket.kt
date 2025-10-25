package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.View
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundMovePlayerRotPacket(
    val view: View,
    val flags: MovePlayerFlags,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.VIEW, ServerboundMovePlayerRotPacket::view,
            MovePlayerFlags.STREAM_CODEC, ServerboundMovePlayerRotPacket::flags,
            ::ServerboundMovePlayerRotPacket
        )
    }
}