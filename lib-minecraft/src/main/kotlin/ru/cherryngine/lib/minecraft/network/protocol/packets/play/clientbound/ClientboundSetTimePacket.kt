package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundSetTimePacket(
    val worldAge: Long,
    val time: Long,
    val isFrozen: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.LONG, ClientboundSetTimePacket::worldAge,
            StreamCodec.LONG, ClientboundSetTimePacket::time,
            StreamCodec.BOOLEAN, ClientboundSetTimePacket::isFrozen,
            ::ClientboundSetTimePacket
        )
    }
}