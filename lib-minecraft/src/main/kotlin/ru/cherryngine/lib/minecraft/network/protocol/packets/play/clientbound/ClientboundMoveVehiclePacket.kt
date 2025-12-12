package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundMoveVehiclePacket(
    val position: Vec3D,
    val yawPitch: YawPitch
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationStreamCodecs.VEC_3D, ClientboundMoveVehiclePacket::position,
            LocationStreamCodecs.YAW_PITCH, ClientboundMoveVehiclePacket::yawPitch,
            ::ClientboundMoveVehiclePacket
        )
    }
}