package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSetEntityMotionPacket(
    val entityId: Int,
    val velocity: Vec3D
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundSetEntityMotionPacket::entityId,
            LocationCodecs.VELOCITY, ClientboundSetEntityMotionPacket::velocity,
            ::ClientboundSetEntityMotionPacket
        )
    }
}