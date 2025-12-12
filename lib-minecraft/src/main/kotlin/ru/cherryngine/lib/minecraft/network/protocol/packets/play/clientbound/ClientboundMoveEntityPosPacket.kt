package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundMoveEntityPosPacket(
    val entityId: Int,
    val delta: Vec3D,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundMoveEntityPosPacket::entityId,
            LocationStreamCodecs.MOVE_ENTITY_DELTA, ClientboundMoveEntityPosPacket::delta,
            StreamCodec.BOOLEAN, ClientboundMoveEntityPosPacket::isOnGround,
            ::ClientboundMoveEntityPosPacket
        )
    }
}