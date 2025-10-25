package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.Difficulty
import io.github.dockyardmc.tide.stream.ByteEnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundChangeDifficultyPacket(
    val difficulty: Difficulty,
    val locked: Boolean = false
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ByteEnumStreamCodec<Difficulty>(), ClientboundChangeDifficultyPacket::difficulty,
            StreamCodec.BOOLEAN, ClientboundChangeDifficultyPacket::locked,
            ::ClientboundChangeDifficultyPacket
        )
    }
}