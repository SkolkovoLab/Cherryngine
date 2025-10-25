package ru.cherryngine.lib.minecraft.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ServerboundFinishConfigurationPacket : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ServerboundFinishConfigurationPacket
        )
    }
}