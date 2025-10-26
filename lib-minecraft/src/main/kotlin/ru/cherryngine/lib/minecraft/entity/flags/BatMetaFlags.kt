package ru.cherryngine.lib.minecraft.entity.flags

data class BatMetaFlags(
    val isHanging: Boolean = false,
) {
    companion object {
        val DEFAULT = BatMetaFlags()

        fun toByte(flags: BatMetaFlags): Byte {
            var byte = 0
            if (flags.isHanging) byte = byte or 0x01
            return byte.toByte()
        }

        fun fromByte(byte: Byte): BatMetaFlags {
            val byte = byte.toInt()
            return BatMetaFlags(
                isHanging = (byte and 0x01) != 0,
            )
        }
    }
}