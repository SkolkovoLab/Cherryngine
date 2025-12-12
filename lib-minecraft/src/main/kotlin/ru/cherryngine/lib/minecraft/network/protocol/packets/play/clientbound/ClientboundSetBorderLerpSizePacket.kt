package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundSetBorderLerpSizePacket(
    val oldDiameter: Double,
    val newDiameter: Double,
    val speed: Long,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.DOUBLE, ClientboundSetBorderLerpSizePacket::oldDiameter,
            StreamCodec.DOUBLE, ClientboundSetBorderLerpSizePacket::newDiameter,
            StreamCodec.VAR_LONG, ClientboundSetBorderLerpSizePacket::speed,
            ::ClientboundSetBorderLerpSizePacket
        )
    }
}