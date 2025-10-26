package ru.cherryngine.lib.minecraft.entity.flags

data class TameableAnimalMetaFlags(
    val isSitting: Boolean = false,
    val isTamed: Boolean = false,
) {
    companion object {
        val DEFAULT = TameableAnimalMetaFlags()

        fun toByte(flags: TameableAnimalMetaFlags): Byte {
            var byte = 0
            if (flags.isSitting) byte = byte or 0x01
            if (flags.isTamed) byte = byte or 0x04
            return byte.toByte()
        }

        fun fromByte(byte: Byte): TameableAnimalMetaFlags {
            val byte = byte.toInt()
            return TameableAnimalMetaFlags(
                isSitting = (byte and 0x01) != 0,
                isTamed = (byte and 0x04) != 0,
            )
        }
    }
}