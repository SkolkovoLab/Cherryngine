package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.TeleportFlags
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundPlayerPositionPacket(
    val teleportId: Int,
    val location: Vec3D,
    val delta: Vec3D,
    val view: View,
    val teleportFlags: TeleportFlags,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundPlayerPositionPacket::teleportId,
            LocationCodecs.VEC_3D, ClientboundPlayerPositionPacket::location,
            LocationCodecs.VEC_3D, ClientboundPlayerPositionPacket::delta,
            LocationCodecs.VIEW, ClientboundPlayerPositionPacket::view,
            TeleportFlags.STREAM_CODEC, ClientboundPlayerPositionPacket::teleportFlags,
            ::ClientboundPlayerPositionPacket
        )
    }
}