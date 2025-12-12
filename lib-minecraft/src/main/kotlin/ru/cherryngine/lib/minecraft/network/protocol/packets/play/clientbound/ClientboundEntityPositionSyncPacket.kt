package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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
            LocationStreamCodecs.VEC_3D, ClientboundEntityPositionSyncPacket::location,
            LocationStreamCodecs.VEC_3D, ClientboundEntityPositionSyncPacket::delta,
            LocationStreamCodecs.YAW_PITCH, ClientboundEntityPositionSyncPacket::yawPitch,
            StreamCodec.BOOLEAN, ClientboundEntityPositionSyncPacket::isOnGround,
            ::ClientboundEntityPositionSyncPacket
        )
    }
}