package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundClientCommandPacket(
    val action: Action
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Action>(), ServerboundClientCommandPacket::action,
            ::ServerboundClientCommandPacket
        )
    }

    enum class Action {
        PERFORM_RESPAWN,
        REQUEST_STATS
    }
}