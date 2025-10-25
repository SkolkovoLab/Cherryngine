package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundCommandSuggestionPacket(
    val transactionId: Int,
    val text: String
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundCommandSuggestionPacket::transactionId,
            StreamCodec.STRING, ServerboundCommandSuggestionPacket::text,
            ::ServerboundCommandSuggestionPacket
        )
    }
}