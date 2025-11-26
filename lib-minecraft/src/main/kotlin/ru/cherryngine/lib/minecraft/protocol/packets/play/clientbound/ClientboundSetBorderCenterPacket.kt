package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSetBorderCenterPacket(
    val x: Double,
    val z: Double,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.DOUBLE, ClientboundSetBorderCenterPacket::x,
            StreamCodec.DOUBLE, ClientboundSetBorderCenterPacket::z,
            ::ClientboundSetBorderCenterPacket
        )
    }
}