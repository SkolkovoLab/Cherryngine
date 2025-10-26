package ru.cherryngine.lib.minecraft.protocol.types

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class DisplayedSkinParts(
    val cape: Boolean,
    val jacket: Boolean,
    val leftSleeve: Boolean,
    val rightSleeve: Boolean,
    val leftPants: Boolean,
    val rightPants: Boolean,
    val hat: Boolean,
) {
    @Suppress("BooleanLiteralArgument")
    companion object {
        val NONE = DisplayedSkinParts(false, false, false, false, false, false, false)
        val ALL = DisplayedSkinParts(true, true, true, true, true, true, true)

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, DisplayedSkinParts::cape,
            0x02, DisplayedSkinParts::jacket,
            0x04, DisplayedSkinParts::leftSleeve,
            0x08, DisplayedSkinParts::rightSleeve,
            0x10, DisplayedSkinParts::leftPants,
            0x20, DisplayedSkinParts::rightPants,
            0x40, DisplayedSkinParts::hat,
            ::DisplayedSkinParts
        )
    }
}