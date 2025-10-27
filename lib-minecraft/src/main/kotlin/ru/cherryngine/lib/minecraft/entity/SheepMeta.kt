package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.protocol.types.DyeColor

@Suppress("PropertyName")
sealed class SheepMeta : AgeableMobMeta() {
    companion object : SheepMeta()

    val SHEEP_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )

    data class Flags(
        val color: DyeColor = DyeColor.WHITE,
        val isSheared: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                val colorBits = flags.color.ordinal and 0x0F
                val shearBit = if (flags.isSheared) 0x10 else 0
                return (colorBits or shearBit).toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                val colorId = byte and 0x0F
                val color = DyeColor.entries[colorId]
                val isSheared = (byte and 0x10) != 0
                return Flags(color, isSheared)
            }
        }
    }
}