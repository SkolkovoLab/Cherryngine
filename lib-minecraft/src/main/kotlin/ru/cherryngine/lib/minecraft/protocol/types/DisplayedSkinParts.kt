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

        fun toByte(parts: DisplayedSkinParts): Byte {
            var byte = 0
            if (parts.cape) byte = byte or 0x01
            if (parts.jacket) byte = byte or 0x02
            if (parts.leftSleeve) byte = byte or 0x04
            if (parts.rightSleeve) byte = byte or 0x08
            if (parts.leftPants) byte = byte or 0x10
            if (parts.rightPants) byte = byte or 0x20
            if (parts.hat) byte = byte or 0x40
            return byte.toByte()
        }

        fun fromByte(byte: Byte): DisplayedSkinParts {
            val byte = byte.toInt()
            return DisplayedSkinParts(
                cape = (byte and 0x01) != 0,
                jacket = (byte and 0x02) != 0,
                leftSleeve = (byte and 0x04) != 0,
                rightSleeve = (byte and 0x08) != 0,
                leftPants = (byte and 0x10) != 0,
                rightPants = (byte and 0x20) != 0,
                hat = (byte and 0x40) != 0,
            )
        }

        val STREAM_CODEC = StreamCodec.BYTE.transform(::fromByte, ::toByte)
    }
}