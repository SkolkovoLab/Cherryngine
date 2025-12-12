package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ClientboundBundleDelimiterPacket : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ClientboundBundleDelimiterPacket
        )
    }
}
