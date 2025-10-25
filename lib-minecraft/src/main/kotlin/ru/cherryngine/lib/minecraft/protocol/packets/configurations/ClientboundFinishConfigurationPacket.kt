package ru.cherryngine.lib.minecraft.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ClientboundFinishConfigurationPacket : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(::ClientboundFinishConfigurationPacket)
    }
}