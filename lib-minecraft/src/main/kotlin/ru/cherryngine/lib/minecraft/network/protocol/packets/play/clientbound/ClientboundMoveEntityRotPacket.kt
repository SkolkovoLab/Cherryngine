package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundMoveEntityRotPacket(
    val entityId: Int,
    val yawPitch: YawPitch,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundMoveEntityRotPacket::entityId,
            LocationStreamCodecs.YAW_PITCH, ClientboundMoveEntityRotPacket::yawPitch,
            StreamCodec.BOOLEAN, ClientboundMoveEntityRotPacket::isOnGround,
            ::ClientboundMoveEntityRotPacket
        )
    }
}