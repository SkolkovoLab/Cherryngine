package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.TeleportFlags
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundTeleportEntityPacket(
    val entityId: Int,
    val location: Vec3D,
    val velocity: Vec3D,
    val view: View,
    val teleportFlags: TeleportFlags,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundTeleportEntityPacket::entityId,
            LocationCodecs.VEC_3D, ClientboundTeleportEntityPacket::location,
            LocationCodecs.VEC_3D, ClientboundTeleportEntityPacket::velocity,
            LocationCodecs.VIEW, ClientboundTeleportEntityPacket::view,
            TeleportFlags.STREAM_CODEC, ClientboundTeleportEntityPacket::teleportFlags,
            StreamCodec.BOOLEAN, ClientboundTeleportEntityPacket::isOnGround,
            ::ClientboundTeleportEntityPacket
        )
    }
}