package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundRotateHeadPacket(
    val entityId: Int,
    val yaw: Float
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundRotateHeadPacket::entityId,
            LocationCodecs.ANGLE, ClientboundRotateHeadPacket::yaw,
            ::ClientboundRotateHeadPacket
        )
    }
}