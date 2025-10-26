package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundMoveEntityRotPacket(
    val entityId: Int,
    val yawPitch: YawPitch,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundMoveEntityRotPacket::entityId,
            LocationCodecs.YAW_PITCH, ClientboundMoveEntityRotPacket::yawPitch,
            StreamCodec.BOOLEAN, ClientboundMoveEntityRotPacket::isOnGround,
            ::ClientboundMoveEntityRotPacket
        )
    }
}