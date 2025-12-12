package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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