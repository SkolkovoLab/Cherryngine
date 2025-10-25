package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.MovePlayerFlags
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundMovePlayerStatusOnlyPacket(
    val flags: MovePlayerFlags
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            MovePlayerFlags.STREAM_CODEC, ServerboundMovePlayerStatusOnlyPacket::flags,
            ::ServerboundMovePlayerStatusOnlyPacket
        )
    }
}