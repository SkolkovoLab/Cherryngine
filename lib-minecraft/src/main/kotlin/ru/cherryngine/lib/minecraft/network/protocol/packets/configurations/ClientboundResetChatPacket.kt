package ru.cherryngine.lib.minecraft.network.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ClientboundResetChatPacket : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(::ClientboundResetChatPacket)
    }
}