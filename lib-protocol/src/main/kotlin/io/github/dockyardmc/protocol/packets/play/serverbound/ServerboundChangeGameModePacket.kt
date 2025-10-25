package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.GameMode
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundChangeGameModePacket(
    val gameMode: GameMode
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<GameMode>(), ServerboundChangeGameModePacket::gameMode,
            ::ServerboundChangeGameModePacket
        )
    }
}