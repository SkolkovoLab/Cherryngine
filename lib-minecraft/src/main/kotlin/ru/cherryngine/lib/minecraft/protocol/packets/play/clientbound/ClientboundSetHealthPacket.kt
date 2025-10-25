package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSetHealthPacket(
    val health: Float,
    val food: Int,
    val saturation: Float
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, ClientboundSetHealthPacket::health,
            StreamCodec.VAR_INT, ClientboundSetHealthPacket::food,
            StreamCodec.FLOAT, ClientboundSetHealthPacket::saturation,
            ::ClientboundSetHealthPacket
        )
    }
}