package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundTakeItemEntityPacket(
    val collected: Int,
    val collector: Int,
    val amount: Int
) : ClientboundPacket {

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundTakeItemEntityPacket::collected,
            StreamCodec.VAR_INT, ClientboundTakeItemEntityPacket::collector,
            StreamCodec.VAR_INT, ClientboundTakeItemEntityPacket::amount,
            ::ClientboundTakeItemEntityPacket
        )
    }
}