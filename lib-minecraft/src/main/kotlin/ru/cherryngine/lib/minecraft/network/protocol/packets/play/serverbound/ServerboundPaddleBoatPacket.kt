package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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