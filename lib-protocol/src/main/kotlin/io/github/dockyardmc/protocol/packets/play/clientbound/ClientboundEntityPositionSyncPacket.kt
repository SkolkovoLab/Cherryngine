package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundEntityPositionSyncPacket(
    val entity: Int,
    val location: Vec3D,
    val delta: Vec3D,
    val view: View,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundEntityPositionSyncPacket::entity,
            LocationCodecs.VEC_3D, ClientboundEntityPositionSyncPacket::location,
            LocationCodecs.VEC_3D, ClientboundEntityPositionSyncPacket::delta,
            LocationCodecs.VIEW, ClientboundEntityPositionSyncPacket::view,
            StreamCodec.BOOLEAN, ClientboundEntityPositionSyncPacket::isOnGround,
            ::ClientboundEntityPositionSyncPacket
        )
    }
}