package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.TeleportFlags
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundTeleportEntityPacket(
    val entityId: Int,
    val location: Vec3D,
    val velocity: Vec3D,
    val yawPitch: YawPitch,
    val teleportFlags: TeleportFlags,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundTeleportEntityPacket::entityId,
            LocationCodecs.VEC_3D, ClientboundTeleportEntityPacket::location,
            LocationCodecs.VEC_3D, ClientboundTeleportEntityPacket::velocity,
            LocationCodecs.YAW_PITCH, ClientboundTeleportEntityPacket::yawPitch,
            TeleportFlags.STREAM_CODEC, ClientboundTeleportEntityPacket::teleportFlags,
            StreamCodec.BOOLEAN, ClientboundTeleportEntityPacket::isOnGround,
            ::ClientboundTeleportEntityPacket
        )
    }
}