package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ServerboundClientTickEndPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ServerboundClientTickEndPacket
        )
    }
}