package ru.cherryngine.lib.minecraft.entity.flags

data class SnowGolemMetaFlags(
    val hasPumpkin: Boolean = true,
) {
    companion object {
        val DEFAULT = SnowGolemMetaFlags()

        fun toByte(flags: SnowGolemMetaFlags): Byte {
            var byte = 0
            if (flags.hasPumpkin) byte = byte or 0x10
            return byte.toByte()
        }

        fun fromByte(byte: Byte): SnowGolemMetaFlags {
            val byte = byte.toInt()
            return SnowGolemMetaFlags(
                hasPumpkin = (byte and 0x10) != 0,
            )
        }
    }
}