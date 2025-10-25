package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundSetCarriedItemPacket(
    val slot: Int
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.INT_SHORT, ServerboundSetCarriedItemPacket::slot,
            ::ServerboundSetCarriedItemPacket
        )
    }
}