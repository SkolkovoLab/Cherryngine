package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundMoveVehiclePacket(
    val position: Vec3D,
    val view: View
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.VEC_3D, ClientboundMoveVehiclePacket::position,
            LocationCodecs.VIEW, ClientboundMoveVehiclePacket::view,
            ::ClientboundMoveVehiclePacket
        )
    }
}