package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.cryptography.PlayerSession
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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