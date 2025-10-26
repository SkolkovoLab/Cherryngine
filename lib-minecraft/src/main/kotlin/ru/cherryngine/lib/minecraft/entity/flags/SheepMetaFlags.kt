package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.protocol.types.DyeColor
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class SheepMetaFlags(
    val color: DyeColor = DyeColor.WHITE,
    val isSheared: Boolean = false,
) {
    companion object {
        val DEFAULT = SheepMetaFlags()

        val STREAM_CODEC = StreamCodec.INT_BYTE.transform<SheepMetaFlags>(
            {
                val colorId = it and 0x0F
                val color = DyeColor.entries[colorId]
                val isSheared = (it and 0x10) != 0
                SheepMetaFlags(color, isSheared)
            },
            {
                val colorBits = it.color.ordinal and 0x0F
                val shearBit = if (it.isSheared) 0x10 else 0
                colorBits or shearBit
            }
        )

        fun metaEntry(value: SheepMetaFlags): Metadata.Entry<SheepMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}