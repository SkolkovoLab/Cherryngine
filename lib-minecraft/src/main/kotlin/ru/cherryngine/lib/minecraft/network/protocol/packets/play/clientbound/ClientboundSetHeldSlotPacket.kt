package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundSetHeldSlotPacket(
    val slot: Byte
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BYTE, ClientboundSetHeldSlotPacket::slot,
            ::ClientboundSetHeldSlotPacket
        )
    }
}