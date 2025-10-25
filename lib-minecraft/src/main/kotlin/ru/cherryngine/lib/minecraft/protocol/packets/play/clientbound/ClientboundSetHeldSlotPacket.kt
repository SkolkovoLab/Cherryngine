package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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