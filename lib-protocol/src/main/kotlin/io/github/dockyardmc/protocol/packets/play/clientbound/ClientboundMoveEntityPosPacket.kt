package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundMoveEntityPosPacket(
    val entityId: Int,
    val delta: Vec3D,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundMoveEntityPosPacket::entityId,
            LocationCodecs.MOVE_ENTITY_DELTA, ClientboundMoveEntityPosPacket::delta,
            StreamCodec.BOOLEAN, ClientboundMoveEntityPosPacket::isOnGround,
            ::ClientboundMoveEntityPosPacket
        )
    }
}