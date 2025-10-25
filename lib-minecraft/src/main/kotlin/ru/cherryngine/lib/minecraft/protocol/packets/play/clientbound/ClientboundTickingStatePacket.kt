package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundTickingStatePacket(
    val tickRate: Float,
    val isFrozen: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, ClientboundTickingStatePacket::tickRate,
            StreamCodec.BOOLEAN, ClientboundTickingStatePacket::isFrozen,
            ::ClientboundTickingStatePacket
        )
    }
}