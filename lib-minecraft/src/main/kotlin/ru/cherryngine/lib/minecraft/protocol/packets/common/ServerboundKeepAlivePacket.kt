package ru.cherryngine.lib.minecraft.protocol.packets.common

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundKeepAlivePacket(
    val keepAliveId: Long
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.LONG, ServerboundKeepAlivePacket::keepAliveId,
            ::ServerboundKeepAlivePacket
        )
    }
}