package ru.cherryngine.lib.minecraft.entity.flags

data class BeeMetaFlags(
    val isAngry: Boolean = false,
    val hasStung: Boolean = false,
    val hasNectar: Boolean = false,
) {
    companion object {
        val DEFAULT = BeeMetaFlags()

        fun toByte(flags: BeeMetaFlags): Byte {
            var byte = 0
            if (flags.isAngry) byte = byte or 0x02
            if (flags.hasStung) byte = byte or 0x04
            if (flags.hasNectar) byte = byte or 0x08
            return byte.toByte()
        }

        fun fromByte(byte: Byte): BeeMetaFlags {
            val byte = byte.toInt()
            return BeeMetaFlags(
                isAngry = (byte and 0x02) != 0,
                hasStung = (byte and 0x04) != 0,
                hasNectar = (byte and 0x08) != 0,
            )
        }
    }
}