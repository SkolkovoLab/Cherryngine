package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class SnowGolemMeta : MobMeta() {
    companion object : SnowGolemMeta()

    val SNOW_GOLEM_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )

    data class Flags(
        val hasPumpkin: Boolean = true,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.hasPumpkin) byte = byte or 0x10
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    hasPumpkin = (byte and 0x10) != 0,
                )
            }
        }
    }
}