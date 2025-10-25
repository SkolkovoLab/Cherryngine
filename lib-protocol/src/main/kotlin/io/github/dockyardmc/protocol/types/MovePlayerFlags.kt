package io.github.dockyardmc.protocol.types

import io.github.dockyardmc.tide.stream.StreamCodec

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