package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.Direction
import ru.cherryngine.lib.minecraft.tide.stream.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundPlayerActionPacket(
    val action: Action,
    val position: Vec3I,
    val face: Direction,
    val sequence: Int
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Action>(), ServerboundPlayerActionPacket::action,
            LocationCodecs.BLOCK_POSITION, ServerboundPlayerActionPacket::position,
            ByteEnumStreamCodec<Direction>(), ServerboundPlayerActionPacket::face,
            StreamCodec.VAR_INT, ServerboundPlayerActionPacket::sequence,
            ::ServerboundPlayerActionPacket
        )
    }

    enum class Action {
        START_DIGGING,
        CANCELLED_DIGGING,
        FINISHED_DIGGING,
        DROP_ITEM_STACK,
        DROP_ITEM,
        HELD_ITEM_UPDATE,
        SWAP_ITEM
    }
}