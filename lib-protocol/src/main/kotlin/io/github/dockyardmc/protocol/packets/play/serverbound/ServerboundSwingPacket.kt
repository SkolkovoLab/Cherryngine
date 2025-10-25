package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.PlayerHand
import io.github.dockyardmc.tide.stream.ByteEnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundSwingPacket(
    val hand: PlayerHand
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ByteEnumStreamCodec<PlayerHand>(), ServerboundSwingPacket::hand,
            ::ServerboundSwingPacket
        )
    }
}