package ru.cherryngine.lib.minecraft.entity.flags

data class TextDisplayMetaFlags(
    val hasShadow: Boolean = false,
    val isSeeThrough: Boolean = false,
    val useDefaultBackground: Boolean = false,
    val alignment: Alignment = Alignment.CENTER,
) {
    enum class Alignment {
        CENTER,
        LEFT,
        RIGHT,
    }

    companion object {
        val DEFAULT = TextDisplayMetaFlags()

        fun toByte(flags: TextDisplayMetaFlags): Byte {
            var byte = 0
            if (flags.hasShadow) byte = byte or 0x01
            if (flags.isSeeThrough) byte = byte or 0x02
            if (flags.useDefaultBackground) byte = byte or 0x04
            if (flags.alignment == Alignment.LEFT) byte = byte or 0x08
            if (flags.alignment == Alignment.RIGHT) byte = byte or 0x10
            return byte.toByte()
        }

        fun fromByte(byte: Byte): TextDisplayMetaFlags {
            val byte = byte.toInt()
            val alignment = when {
                (byte and 0x08) != 0 -> Alignment.LEFT
                (byte and 0x10) != 0 -> Alignment.RIGHT
                else -> Alignment.CENTER
            }
            return TextDisplayMetaFlags(
                hasShadow = (byte and 0x01) != 0,
                isSeeThrough = (byte and 0x02) != 0,
                useDefaultBackground = (byte and 0x04) != 0,
                alignment = alignment,
            )
        }
    }
}