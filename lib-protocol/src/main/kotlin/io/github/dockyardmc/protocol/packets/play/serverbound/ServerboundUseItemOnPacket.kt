package io.github.dockyardmc.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3I
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.Direction
import io.github.dockyardmc.protocol.types.PlayerHand
import io.github.dockyardmc.tide.stream.ByteEnumStreamCodec
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundUseItemOnPacket(
    val hand: PlayerHand,
    val pos: Vec3I,
    val face: Direction,
    val cursorX: Float,
    val cursorY: Float,
    val cursorZ: Float,
    val insideBlock: Boolean,
    val hitWorldBorder: Boolean,
    val sequence: Int
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ByteEnumStreamCodec<PlayerHand>(), ServerboundUseItemOnPacket::hand,
            LocationCodecs.BLOCK_POSITION, ServerboundUseItemOnPacket::pos,
            EnumStreamCodec<Direction>(), ServerboundUseItemOnPacket::face,
            StreamCodec.FLOAT, ServerboundUseItemOnPacket::cursorX,
            StreamCodec.FLOAT, ServerboundUseItemOnPacket::cursorY,
            StreamCodec.FLOAT, ServerboundUseItemOnPacket::cursorZ,
            StreamCodec.BOOLEAN, ServerboundUseItemOnPacket::insideBlock,
            StreamCodec.BOOLEAN, ServerboundUseItemOnPacket::hitWorldBorder,
            StreamCodec.VAR_INT, ServerboundUseItemOnPacket::sequence,
            ::ServerboundUseItemOnPacket
        )
    }
}