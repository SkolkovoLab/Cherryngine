package ru.cherryngine.lib.minecraft.entity.flags

data class FoxMetaFlags(
    val isSitting: Boolean = false,
    val isCrouching: Boolean = false,
    val isInterested: Boolean = false,
    val isPouncing: Boolean = false,
    val isSleeping: Boolean = false,
    val isFaceplanted: Boolean = false,
    val isDefending: Boolean = false,
) {
    companion object {
        val DEFAULT = FoxMetaFlags()

        fun toByte(flags: FoxMetaFlags): Byte {
            var byte = 0
            if (flags.isSitting) byte = byte or 0x01
            if (flags.isCrouching) byte = byte or 0x04
            if (flags.isInterested) byte = byte or 0x08
            if (flags.isPouncing) byte = byte or 0x10
            if (flags.isSleeping) byte = byte or 0x20
            if (flags.isFaceplanted) byte = byte or 0x40
            if (flags.isDefending) byte = byte or 0x80
            return byte.toByte()
        }

        fun fromByte(byte: Byte): FoxMetaFlags {
            val byte = byte.toInt()
            return FoxMetaFlags(
                isSitting = (byte and 0x01) != 0,
                isCrouching = (byte and 0x04) != 0,
                isInterested = (byte and 0x08) != 0,
                isPouncing = (byte and 0x10) != 0,
                isSleeping = (byte and 0x20) != 0,
                isFaceplanted = (byte and 0x40) != 0,
                isDefending = (byte and 0x80) != 0,
            )
        }
    }
}