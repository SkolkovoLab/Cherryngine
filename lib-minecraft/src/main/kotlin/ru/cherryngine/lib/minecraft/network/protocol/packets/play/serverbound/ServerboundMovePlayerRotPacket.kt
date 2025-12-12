package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundMovePlayerRotPacket(
    val yawPitch: YawPitch,
    val flags: MovePlayerFlags,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationStreamCodecs.YAW_PITCH, ServerboundMovePlayerRotPacket::yawPitch,
            MovePlayerFlags.STREAM_CODEC, ServerboundMovePlayerRotPacket::flags,
            ::ServerboundMovePlayerRotPacket
        )
    }
}