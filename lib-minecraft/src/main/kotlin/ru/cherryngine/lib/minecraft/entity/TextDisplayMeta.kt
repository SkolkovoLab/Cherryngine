package ru.cherryngine.lib.minecraft.entity

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.ShadowColor

@Suppress("PropertyName")
sealed class TextDisplayMeta : DisplayMeta() {
    companion object : TextDisplayMeta()

    val TEXT = index(MetadataEntry.Type.COMPONENT, Component.empty())
    val LINE_WIDTH = index(MetadataEntry.Type.VAR_INT, 200)
    val BACKGROUND_COLOR = index(
        MetadataEntry.Type.VAR_INT,
        ShadowColor.shadowColor(0x40000000),
        ShadowColor::shadowColor,
        ShadowColor::value
    )
    val TEXT_OPACITY = index(MetadataEntry.Type.BYTE, -1)
    val TEXT_DISPLAY_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )

    data class Flags(
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
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.hasShadow) byte = byte or 0x01
                if (flags.isSeeThrough) byte = byte or 0x02
                if (flags.useDefaultBackground) byte = byte or 0x04
                if (flags.alignment == Alignment.LEFT) byte = byte or 0x08
                if (flags.alignment == Alignment.RIGHT) byte = byte or 0x10
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                val alignment = when {
                    (byte and 0x08) != 0 -> Alignment.LEFT
                    (byte and 0x10) != 0 -> Alignment.RIGHT
                    else -> Alignment.CENTER
                }
                return Flags(
                    hasShadow = (byte and 0x01) != 0,
                    isSeeThrough = (byte and 0x02) != 0,
                    useDefaultBackground = (byte and 0x04) != 0,
                    alignment = alignment,
                )
            }
        }
    }
}