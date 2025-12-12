package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundMovePlayerPosPacket(
    val pos: Vec3D,
    val flags: MovePlayerFlags
) : ServerboundPacket {

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationStreamCodecs.VEC_3D, ServerboundMovePlayerPosPacket::pos,
            MovePlayerFlags.STREAM_CODEC, ServerboundMovePlayerPosPacket::flags,
            ::ServerboundMovePlayerPosPacket
        )
    }
}

