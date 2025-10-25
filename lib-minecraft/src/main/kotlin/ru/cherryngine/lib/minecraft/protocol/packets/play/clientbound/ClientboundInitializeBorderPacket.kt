package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundInitializeBorderPacket(
    val x: Double,
    val z: Double,
    val oldDiameter: Double,
    val newDiameter: Double,
    val speed: Long,
    val portalTeleportBoundary: Int,
    val warningBlocks: Int,
    val warningTime: Int
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.DOUBLE, ClientboundInitializeBorderPacket::x,
            StreamCodec.DOUBLE, ClientboundInitializeBorderPacket::z,
            StreamCodec.DOUBLE, ClientboundInitializeBorderPacket::oldDiameter,
            StreamCodec.DOUBLE, ClientboundInitializeBorderPacket::newDiameter,
            StreamCodec.VAR_LONG, ClientboundInitializeBorderPacket::speed,
            StreamCodec.VAR_INT, ClientboundInitializeBorderPacket::portalTeleportBoundary,
            StreamCodec.VAR_INT, ClientboundInitializeBorderPacket::warningBlocks,
            StreamCodec.VAR_INT, ClientboundInitializeBorderPacket::warningTime,
            ::ClientboundInitializeBorderPacket
        )
    }
}