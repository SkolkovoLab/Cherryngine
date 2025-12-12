package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.EntityType
import ru.cherryngine.lib.minecraft.registry.registries.EntityTypeRegistry
import java.util.*

data class ClientboundAddEntityPacket(
    val entityId: Int,
    val entityUUID: UUID,
    val entityType: EntityType,
    val location: Vec3D,
    val yawPitch: YawPitch,
    val headYaw: Float,
    val entityData: Int,
    val velocity: Vec3D,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundAddEntityPacket::entityId,
            StreamCodec.UUID, ClientboundAddEntityPacket::entityUUID,
            EntityTypeRegistry.STREAM_CODEC, ClientboundAddEntityPacket::entityType,
            LocationStreamCodecs.VEC_3D, ClientboundAddEntityPacket::location,
            LocationStreamCodecs.ANGLE_PITCH_YAW, ClientboundAddEntityPacket::yawPitch,
            LocationStreamCodecs.ANGLE, ClientboundAddEntityPacket::headYaw,
            StreamCodec.VAR_INT, ClientboundAddEntityPacket::entityData,
            LocationStreamCodecs.VELOCITY, ClientboundAddEntityPacket::velocity,
            ::ClientboundAddEntityPacket
        )
    }
}