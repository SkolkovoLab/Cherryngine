package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundRemoveEntitiesPacket(
    val entities: List<Int>
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT.list(), ClientboundRemoveEntitiesPacket::entities,
            ::ClientboundRemoveEntitiesPacket
        )
    }
}