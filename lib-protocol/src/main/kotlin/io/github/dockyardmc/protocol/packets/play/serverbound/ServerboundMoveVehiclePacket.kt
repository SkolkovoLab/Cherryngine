package io.github.dockyardmc.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundMoveVehiclePacket(
    val pos: Vec3D,
    val view: View,
    val onGround: Boolean
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.VEC_3D, ServerboundMoveVehiclePacket::pos,
            LocationCodecs.VIEW, ServerboundMoveVehiclePacket::view,
            StreamCodec.BOOLEAN, ServerboundMoveVehiclePacket::onGround,
            ::ServerboundMoveVehiclePacket
        )
    }
}