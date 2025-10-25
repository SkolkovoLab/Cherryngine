package io.github.dockyardmc.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3I
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.Direction
import io.github.dockyardmc.tide.stream.ByteEnumStreamCodec
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

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