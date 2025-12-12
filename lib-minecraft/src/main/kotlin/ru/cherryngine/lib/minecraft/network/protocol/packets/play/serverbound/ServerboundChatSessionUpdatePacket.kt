package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.network.protocol.cryptography.PlayerSession
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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