package ru.cherryngine.lib.minecraft.protocol.packets.common

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundTransferPacket(
    val host: String,
    val port: Int,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ClientboundTransferPacket::host,
            StreamCodec.VAR_INT, ClientboundTransferPacket::port,
            ::ClientboundTransferPacket
        )
    }
}