package ru.cherryngine.lib.minecraft.network.protocol.packets.login

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ServerboundLoginAcknowledgedPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ServerboundLoginAcknowledgedPacket
        )
    }
}