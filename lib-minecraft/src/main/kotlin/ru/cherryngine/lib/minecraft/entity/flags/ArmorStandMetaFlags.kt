package ru.cherryngine.lib.minecraft.entity.flags

data class ArmorStandMetaFlags(
    val isSmall: Boolean = false,
    val hasArms: Boolean = false,
    val noBasePlate: Boolean = false,
    val isMarker: Boolean = false,
) {
    companion object {
        val DEFAULT = ArmorStandMetaFlags()

        fun toByte(flags: ArmorStandMetaFlags): Byte {
            var byte = 0
            if (flags.isSmall) byte = byte or 0x01
            if (flags.hasArms) byte = byte or 0x04
            if (flags.noBasePlate) byte = byte or 0x08
            if (flags.isMarker) byte = byte or 0x10
            return byte.toByte()
        }

        fun fromByte(byte: Byte): ArmorStandMetaFlags {
            val byte = byte.toInt()
            return ArmorStandMetaFlags(
                isSmall = (byte and 0x01) != 0,
                hasArms = (byte and 0x04) != 0,
                noBasePlate = (byte and 0x08) != 0,
                isMarker = (byte and 0x10) != 0,
            )
        }
    }
}