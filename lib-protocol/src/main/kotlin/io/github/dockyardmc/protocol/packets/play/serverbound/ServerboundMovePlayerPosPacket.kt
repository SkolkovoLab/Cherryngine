package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.cherry.math.Vec3D
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.MovePlayerFlags
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundMovePlayerPosPacket(
    val pos: Vec3D,
    val flags: MovePlayerFlags
) : ServerboundPacket {

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.VEC_3D, ServerboundMovePlayerPosPacket::pos,
            MovePlayerFlags.STREAM_CODEC, ServerboundMovePlayerPosPacket::flags,
            ::ServerboundMovePlayerPosPacket
        )
    }
}

