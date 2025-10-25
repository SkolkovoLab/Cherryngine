package ru.cherryngine.lib.minecraft.protocol.packets.status

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundStatusResponsePacket(
    val statusJson: String
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ClientboundStatusResponsePacket::statusJson,
            ::ClientboundStatusResponsePacket
        )
    }
}