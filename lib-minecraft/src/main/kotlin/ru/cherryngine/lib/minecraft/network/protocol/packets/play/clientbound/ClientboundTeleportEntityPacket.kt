package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.TeleportFlags
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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
            LocationStreamCodecs.VEC_3D, ClientboundTeleportEntityPacket::location,
            LocationStreamCodecs.VEC_3D, ClientboundTeleportEntityPacket::velocity,
            LocationStreamCodecs.YAW_PITCH, ClientboundTeleportEntityPacket::yawPitch,
            TeleportFlags.STREAM_CODEC, ClientboundTeleportEntityPacket::teleportFlags,
            StreamCodec.BOOLEAN, ClientboundTeleportEntityPacket::isOnGround,
            ::ClientboundTeleportEntityPacket
        )
    }
}