package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundSetEntityMotionPacket(
    val entityId: Int,
    val velocity: Vec3D
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundSetEntityMotionPacket::entityId,
            LocationStreamCodecs.VELOCITY, ClientboundSetEntityMotionPacket::velocity,
            ::ClientboundSetEntityMotionPacket
        )
    }
}