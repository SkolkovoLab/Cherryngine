package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundKeepAlivePacket(
    val keepAliveId: Long
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.LONG, ClientboundKeepAlivePacket::keepAliveId,
            ::ClientboundKeepAlivePacket
        )
    }
}