package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundMovePlayerRotPacket(
    val yawPitch: YawPitch,
    val flags: MovePlayerFlags,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.YAW_PITCH, ServerboundMovePlayerRotPacket::yawPitch,
            MovePlayerFlags.STREAM_CODEC, ServerboundMovePlayerRotPacket::flags,
            ::ServerboundMovePlayerRotPacket
        )
    }
}