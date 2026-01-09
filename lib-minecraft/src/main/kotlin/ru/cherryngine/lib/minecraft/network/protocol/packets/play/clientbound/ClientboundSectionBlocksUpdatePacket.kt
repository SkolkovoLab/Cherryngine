package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundSectionBlocksUpdatePacket(
    val sectionPos: SectionPos,
    val blocks: List<Long>,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            SectionPos.STREAM_CODEC, ClientboundSectionBlocksUpdatePacket::sectionPos,
            StreamCodec.VAR_LONG.list(), ClientboundSectionBlocksUpdatePacket::blocks,
            ::ClientboundSectionBlocksUpdatePacket
        )
    }
}