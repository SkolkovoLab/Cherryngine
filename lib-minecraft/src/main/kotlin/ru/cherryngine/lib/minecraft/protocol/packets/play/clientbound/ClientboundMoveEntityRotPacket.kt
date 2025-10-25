package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.View
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundMoveEntityRotPacket(
    val entityId: Int,
    val view: View,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundMoveEntityRotPacket::entityId,
            LocationCodecs.VIEW, ClientboundMoveEntityRotPacket::view,
            StreamCodec.BOOLEAN, ClientboundMoveEntityRotPacket::isOnGround,
            ::ClientboundMoveEntityRotPacket
        )
    }
}