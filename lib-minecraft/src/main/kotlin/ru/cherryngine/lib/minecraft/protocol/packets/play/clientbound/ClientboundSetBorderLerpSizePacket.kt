package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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