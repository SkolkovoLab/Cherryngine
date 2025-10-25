package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.cryptography.PlayerSession
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundChatSessionUpdatePacket(
    val playerSession: PlayerSession
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            PlayerSession.STREAM_CODEC, ServerboundChatSessionUpdatePacket::playerSession,
            ::ServerboundChatSessionUpdatePacket
        )
    }
}