package ru.cherryngine.lib.minecraft.protocol.types

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class TeleportFlags(
    val relX: Boolean,
    val relY: Boolean,
    val relZ: Boolean,
    val relYaw: Boolean,
    val relPitch: Boolean,
    val relDeltaX: Boolean,
    val relDeltaY: Boolean,
    val relDeltaZ: Boolean,
    val rotateDelta: Boolean,
) {
    companion object {
        val EMPTY = TeleportFlags(false, false, false, false, false, false, false, false, false)
        val ALL = TeleportFlags(true, true, true, true, true, true, true, true, true)

        val STREAM_CODEC = StreamCodec.intFlags(
            0x0001, TeleportFlags::relX,
            0x0002, TeleportFlags::relY,
            0x0004, TeleportFlags::relZ,
            0x0008, TeleportFlags::relYaw,
            0x0010, TeleportFlags::relPitch,
            0x0020, TeleportFlags::relDeltaX,
            0x0040, TeleportFlags::relDeltaY,
            0x0080, TeleportFlags::relDeltaZ,
            0x0100, TeleportFlags::rotateDelta,
            ::TeleportFlags
        )
    }
}