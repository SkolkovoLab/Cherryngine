package ru.cherryngine.lib.minecraft.network.protocol.packets.status

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ServerboundStatusRequestPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(::ServerboundStatusRequestPacket)
    }
}