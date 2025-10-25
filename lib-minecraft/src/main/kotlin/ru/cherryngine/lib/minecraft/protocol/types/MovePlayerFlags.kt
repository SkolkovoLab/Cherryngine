package ru.cherryngine.lib.minecraft.protocol.types

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class MovePlayerFlags(
    val isOnGround: Boolean,
    val horizontalCollision: Boolean
) {
    companion object {
        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, MovePlayerFlags::isOnGround,
            0x02, MovePlayerFlags::horizontalCollision,
            ::MovePlayerFlags
        )
    }
}