package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.registry.registries.EntityType
import ru.cherryngine.lib.minecraft.registry.registries.EntityTypeRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

data class ClientboundAddEntityPacket(
    val entityId: Int,
    val entityUUID: UUID,
    val entityType: EntityType,
    val location: Vec3D,
    val view: View,
    val headYaw: Float,
    val entityData: Int,
    val velocity: Vec3D,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundAddEntityPacket::entityId,
            StreamCodec.UUID, ClientboundAddEntityPacket::entityUUID,
            EntityTypeRegistry.STREAM_CODEC, ClientboundAddEntityPacket::entityType,
            LocationCodecs.VEC_3D, ClientboundAddEntityPacket::location,
            LocationCodecs.VIEW_AS_ANGLE_PITCH_YAW, ClientboundAddEntityPacket::view,
            LocationCodecs.ANGLE, ClientboundAddEntityPacket::headYaw,
            StreamCodec.VAR_INT, ClientboundAddEntityPacket::entityData,
            LocationCodecs.VELOCITY, ClientboundAddEntityPacket::velocity,
            ::ClientboundAddEntityPacket
        )
    }
}