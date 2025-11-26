package ru.cherryngine.lib.minecraft.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ClientboundResetChatPacket : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(::ClientboundResetChatPacket)
    }
}