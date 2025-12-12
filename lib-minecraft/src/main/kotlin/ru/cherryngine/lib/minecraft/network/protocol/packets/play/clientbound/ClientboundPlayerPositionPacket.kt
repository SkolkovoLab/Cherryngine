package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.TeleportFlags
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundPlayerPositionPacket(
    val teleportId: Int,
    val location: Vec3D,
    val delta: Vec3D,
    val yawPitch: YawPitch,
    val teleportFlags: TeleportFlags,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundPlayerPositionPacket::teleportId,
            LocationStreamCodecs.VEC_3D, ClientboundPlayerPositionPacket::location,
            LocationStreamCodecs.VEC_3D, ClientboundPlayerPositionPacket::delta,
            LocationStreamCodecs.YAW_PITCH, ClientboundPlayerPositionPacket::yawPitch,
            TeleportFlags.STREAM_CODEC, ClientboundPlayerPositionPacket::teleportFlags,
            ::ClientboundPlayerPositionPacket
        )
    }
}