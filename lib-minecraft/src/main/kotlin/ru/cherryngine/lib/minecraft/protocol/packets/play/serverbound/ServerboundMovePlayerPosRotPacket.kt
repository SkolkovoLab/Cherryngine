package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundMovePlayerPosRotPacket(
    val pos: Vec3D,
    val view: View,
    val flags: MovePlayerFlags
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.VEC_3D, ServerboundMovePlayerPosRotPacket::pos,
            LocationCodecs.VIEW, ServerboundMovePlayerPosRotPacket::view,
            MovePlayerFlags.STREAM_CODEC, ServerboundMovePlayerPosRotPacket::flags,
            ::ServerboundMovePlayerPosRotPacket
        )
    }
}