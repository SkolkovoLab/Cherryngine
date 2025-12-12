package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.Direction
import ru.cherryngine.lib.minecraft.network.stream_codec.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundPlayerActionPacket(
    val action: Action,
    val position: Vec3I,
    val face: Direction,
    val sequence: Int
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Action>(), ServerboundPlayerActionPacket::action,
            LocationStreamCodecs.BLOCK_POSITION, ServerboundPlayerActionPacket::position,
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
        SWAP_ITEM,
        STAB
    }
}