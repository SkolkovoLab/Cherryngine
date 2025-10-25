package io.github.dockyardmc.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.MovePlayerFlags
import io.github.dockyardmc.tide.stream.StreamCodec

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