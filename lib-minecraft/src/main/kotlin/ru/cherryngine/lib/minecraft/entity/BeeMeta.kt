package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class BeeMeta : AgeableMobMeta() {
    companion object : BeeMeta()

    val BEE_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )
    val ANGER_TIME_TICKS = index(MetadataEntry.Type.VAR_INT, 0)

    data class Flags(
        val isAngry: Boolean = false,
        val hasStung: Boolean = false,
        val hasNectar: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.isAngry) byte = byte or 0x02
                if (flags.hasStung) byte = byte or 0x04
                if (flags.hasNectar) byte = byte or 0x08
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    isAngry = (byte and 0x02) != 0,
                    hasStung = (byte and 0x04) != 0,
                    hasNectar = (byte and 0x08) != 0,
                )
            }
        }
    }
}