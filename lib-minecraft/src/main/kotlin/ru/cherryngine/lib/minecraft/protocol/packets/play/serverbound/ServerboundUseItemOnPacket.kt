package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.Direction
import ru.cherryngine.lib.minecraft.protocol.types.PlayerHand
import ru.cherryngine.lib.minecraft.tide.stream.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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