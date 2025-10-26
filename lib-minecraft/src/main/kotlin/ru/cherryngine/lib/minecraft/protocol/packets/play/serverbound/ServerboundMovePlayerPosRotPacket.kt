package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundMovePlayerPosRotPacket(
    val pos: Vec3D,
    val yawPitch: YawPitch,
    val flags: MovePlayerFlags
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.VEC_3D, ServerboundMovePlayerPosRotPacket::pos,
            LocationCodecs.YAW_PITCH, ServerboundMovePlayerPosRotPacket::yawPitch,
            MovePlayerFlags.STREAM_CODEC, ServerboundMovePlayerPosRotPacket::flags,
            ::ServerboundMovePlayerPosRotPacket
        )
    }
}