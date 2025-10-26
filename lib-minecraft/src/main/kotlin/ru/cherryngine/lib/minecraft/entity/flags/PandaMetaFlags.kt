package ru.cherryngine.lib.minecraft.entity.flags

data class PandaMetaFlags(
    val isSneezing: Boolean = false,
    val isRolling: Boolean = false,
    val isSitting: Boolean = false,
    val isOnBack: Boolean = false,
) {
    companion object {
        val DEFAULT = PandaMetaFlags()

        fun toByte(flags: PandaMetaFlags): Byte {
            var byte = 0
            if (flags.isSneezing) byte = byte or 0x02
            if (flags.isRolling) byte = byte or 0x04
            if (flags.isSitting) byte = byte or 0x08
            if (flags.isOnBack) byte = byte or 0x10
            return byte.toByte()
        }

        fun fromByte(byte: Byte): PandaMetaFlags {
            val byte = byte.toInt()
            return PandaMetaFlags(
                isSneezing = (byte and 0x02) != 0,
                isRolling = (byte and 0x04) != 0,
                isSitting = (byte and 0x08) != 0,
                isOnBack = (byte and 0x10) != 0,
            )
        }
    }
}