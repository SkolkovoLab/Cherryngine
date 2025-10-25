package ru.cherryngine.lib.minecraft.protocol.packets.login

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ServerboundLoginAcknowledgedPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ServerboundLoginAcknowledgedPacket
        )
    }
}