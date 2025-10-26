package ru.cherryngine.lib.minecraft.entity.flags

data class AbstractHorseMetaFlags(
    val isTame: Boolean = false,
    val hasBred: Boolean = false,
    val isEating: Boolean = false,
    val isRearing: Boolean = false,
    val isMouthOpen: Boolean = false,
) {
    companion object {
        val DEFAULT = AbstractHorseMetaFlags()

        fun toByte(flags: AbstractHorseMetaFlags): Byte {
            var byte = 0
            if (flags.isTame) byte = byte or 0x02
            if (flags.hasBred) byte = byte or 0x08
            if (flags.isEating) byte = byte or 0x10
            if (flags.isRearing) byte = byte or 0x20
            if (flags.isMouthOpen) byte = byte or 0x40
            return byte.toByte()
        }

        fun fromByte(byte: Byte): AbstractHorseMetaFlags {
            val byte = byte.toInt()
            return AbstractHorseMetaFlags(
                isTame = (byte and 0x02) != 0,
                hasBred = (byte and 0x08) != 0,
                isEating = (byte and 0x10) != 0,
                isRearing = (byte and 0x20) != 0,
                isMouthOpen = (byte and 0x40) != 0,
            )
        }
    }
}