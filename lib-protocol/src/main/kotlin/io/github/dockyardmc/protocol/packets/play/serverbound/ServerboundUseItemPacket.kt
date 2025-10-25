package io.github.dockyardmc.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.PlayerHand
import io.github.dockyardmc.tide.stream.ByteEnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundUseItemPacket(
    val hand: PlayerHand,
    val sequence: Int,
    val view: View,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ByteEnumStreamCodec<PlayerHand>(), ServerboundUseItemPacket::hand,
            StreamCodec.VAR_INT, ServerboundUseItemPacket::sequence,
            LocationCodecs.VIEW, ServerboundUseItemPacket::view,
            ::ServerboundUseItemPacket
        )
    }
}