package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import java.util.*

data class ClientboundAddEntityPacket(
    val entityId: Int,
    val entityUUID: UUID,
    val entityType: Int,
    val location: Vec3D,
    val view: View,
    val headYaw: Float,
    val entityData: Int,
    val velocity: Vec3D
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundAddEntityPacket::entityId,
            StreamCodec.UUID, ClientboundAddEntityPacket::entityUUID,
            StreamCodec.VAR_INT, ClientboundAddEntityPacket::entityType,
            LocationCodecs.VEC_3D, ClientboundAddEntityPacket::location,
            LocationCodecs.VIEW_AS_ANGLE_PITCH_YAW, ClientboundAddEntityPacket::view,
            LocationCodecs.ANGLE, ClientboundAddEntityPacket::headYaw,
            StreamCodec.VAR_INT, ClientboundAddEntityPacket::entityData,
            LocationCodecs.VELOCITY, ClientboundAddEntityPacket::velocity,
            ::ClientboundAddEntityPacket
        )
    }
}