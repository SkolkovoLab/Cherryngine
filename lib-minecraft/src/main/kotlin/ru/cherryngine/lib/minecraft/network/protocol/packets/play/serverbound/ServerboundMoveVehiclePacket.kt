package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundMoveVehiclePacket(
    val pos: Vec3D,
    val yawPitch: YawPitch,
    val onGround: Boolean
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationStreamCodecs.VEC_3D, ServerboundMoveVehiclePacket::pos,
            LocationStreamCodecs.YAW_PITCH, ServerboundMoveVehiclePacket::yawPitch,
            StreamCodec.BOOLEAN, ServerboundMoveVehiclePacket::onGround,
            ::ServerboundMoveVehiclePacket
        )
    }
}