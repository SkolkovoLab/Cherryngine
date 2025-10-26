package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.protocol.types.DyeColor

data class SheepMetaFlags(
    val color: DyeColor = DyeColor.WHITE,
    val isSheared: Boolean = false,
) {
    companion object {
        val DEFAULT = SheepMetaFlags()

        fun toByte(flags: SheepMetaFlags): Byte {
            val colorBits = flags.color.ordinal and 0x0F
            val shearBit = if (flags.isSheared) 0x10 else 0
            return (colorBits or shearBit).toByte()
        }

        fun fromByte(byte: Byte): SheepMetaFlags {
            val byte = byte.toInt()
            val colorId = byte and 0x0F
            val color = DyeColor.entries[colorId]
            val isSheared = (byte and 0x10) != 0
            return SheepMetaFlags(color, isSheared)
        }
    }
}