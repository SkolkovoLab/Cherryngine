package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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