package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundEntityPositionSyncPacket(
    val entity: Int,
    val location: Vec3D,
    val delta: Vec3D,
    val yawPitch: YawPitch,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundEntityPositionSyncPacket::entity,
            LocationCodecs.VEC_3D, ClientboundEntityPositionSyncPacket::location,
            LocationCodecs.VEC_3D, ClientboundEntityPositionSyncPacket::delta,
            LocationCodecs.YAW_PITCH, ClientboundEntityPositionSyncPacket::yawPitch,
            StreamCodec.BOOLEAN, ClientboundEntityPositionSyncPacket::isOnGround,
            ::ClientboundEntityPositionSyncPacket
        )
    }
}