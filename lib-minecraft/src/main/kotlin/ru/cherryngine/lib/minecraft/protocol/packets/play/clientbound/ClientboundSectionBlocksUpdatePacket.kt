package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSectionBlocksUpdatePacket(
    val sectionIndex: Long,
    val blocks: List<Long>,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.LONG, ClientboundSectionBlocksUpdatePacket::sectionIndex,
            StreamCodec.VAR_LONG.list(), ClientboundSectionBlocksUpdatePacket::blocks,
            ::ClientboundSectionBlocksUpdatePacket
        )
    }
}