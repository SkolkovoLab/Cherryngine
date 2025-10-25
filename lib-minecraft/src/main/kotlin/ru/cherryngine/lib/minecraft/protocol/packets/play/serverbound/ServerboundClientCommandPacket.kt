package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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