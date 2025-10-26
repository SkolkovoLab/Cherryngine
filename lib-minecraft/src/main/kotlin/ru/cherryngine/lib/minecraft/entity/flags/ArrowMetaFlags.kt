package ru.cherryngine.lib.minecraft.entity.flags

data class ArrowMetaFlags(
    val isCritical: Boolean = false,
    val isNoclip: Boolean = false, // used by loyalty tridents when returning
) {
    companion object {
        val DEFAULT = ArrowMetaFlags()

        fun toByte(flags: ArrowMetaFlags): Byte {
            var byte = 0
            if (flags.isCritical) byte = byte or 0x01
            if (flags.isNoclip) byte = byte or 0x02
            return byte.toByte()
        }

        fun fromByte(byte: Byte): ArrowMetaFlags {
            val byte = byte.toInt()
            return ArrowMetaFlags(
                isCritical = (byte and 0x01) != 0,
                isNoclip = (byte and 0x02) != 0,
            )
        }
    }
}