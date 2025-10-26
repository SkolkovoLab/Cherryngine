package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, TextDisplayMetaFlags::hasShadow,
            0x02, TextDisplayMetaFlags::isSeeThrough,
            0x04, TextDisplayMetaFlags::useDefaultBackground,
            0x08, { it.alignment == Alignment.LEFT },
            0x10, { it.alignment == Alignment.RIGHT }
        ) { hasShadow, isSeeThrough, useDefaultBackground, alignLeft, alignRight ->
            val alignment = when {
                alignLeft -> Alignment.LEFT
                alignRight -> Alignment.RIGHT
                else -> Alignment.CENTER
            }
            TextDisplayMetaFlags(hasShadow, isSeeThrough, useDefaultBackground, alignment)
        }

        fun metaEntry(value: TextDisplayMetaFlags): Metadata.Entry<TextDisplayMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}