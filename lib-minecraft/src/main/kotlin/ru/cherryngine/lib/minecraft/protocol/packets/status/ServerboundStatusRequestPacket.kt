package ru.cherryngine.lib.minecraft.protocol.packets.status

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ServerboundStatusRequestPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(::ServerboundStatusRequestPacket)
    }
}