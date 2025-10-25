package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundPlayerInputPacket(
    val input: Input,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Input.STREAM_CODEC, ServerboundPlayerInputPacket::input,
            ::ServerboundPlayerInputPacket
        )
    }

    data class Input(
        val forward: Boolean,
        val backward: Boolean,
        val left: Boolean,
        val right: Boolean,
        val jump: Boolean,
        val sneak: Boolean,
        val sprint: Boolean,
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.byteFlags(
                0x01, Input::forward,
                0x02, Input::backward,
                0x04, Input::left,
                0x08, Input::right,
                0x10, Input::jump,
                0x20, Input::sneak,
                0x40, Input::sprint,
                ::Input
            )
        }
    }
}