package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundPaddleBoatPacket(
    val left: Boolean,
    val right: Boolean,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BOOLEAN, ServerboundPaddleBoatPacket::left,
            StreamCodec.BOOLEAN, ServerboundPaddleBoatPacket::right,
            ::ServerboundPaddleBoatPacket
        )
    }
}