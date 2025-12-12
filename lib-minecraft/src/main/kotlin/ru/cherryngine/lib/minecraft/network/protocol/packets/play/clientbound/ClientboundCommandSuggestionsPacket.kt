package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundCommandSuggestionsPacket(
    val transactionId: Int,
    val start: Int,
    val length: Int,
    val suggestions: List<Suggestion>
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundCommandSuggestionsPacket::transactionId,
            StreamCodec.VAR_INT, ClientboundCommandSuggestionsPacket::start,
            StreamCodec.VAR_INT, ClientboundCommandSuggestionsPacket::length,
            Suggestion.STREAM_CODEC.list(), ClientboundCommandSuggestionsPacket::suggestions,
            ::ClientboundCommandSuggestionsPacket
        )
    }

    data class Suggestion(
        val text: String,
        val tooltip: Component?
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, Suggestion::text,
                ComponentStreamCodecs.NBT.optional(), Suggestion::tooltip,
                ::Suggestion
            )
        }
    }
}