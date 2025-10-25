package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ClientboundBundleDelimiterPacket : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ::ClientboundBundleDelimiterPacket
        )
    }
}
