package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundMoveEntityPosRotPacket(
    val entityId: Int,
    val delta: Vec3D,
    val view: View,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundMoveEntityPosRotPacket::entityId,
            LocationCodecs.MOVE_ENTITY_DELTA, ClientboundMoveEntityPosRotPacket::delta,
            LocationCodecs.VIEW, ClientboundMoveEntityPosRotPacket::view,
            StreamCodec.BOOLEAN, ClientboundMoveEntityPosRotPacket::isOnGround,
            ::ClientboundMoveEntityPosRotPacket
        )
    }
}