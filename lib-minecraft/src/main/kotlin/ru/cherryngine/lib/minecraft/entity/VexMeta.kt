package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class VexMeta : MobMeta() {
    companion object : VexMeta()

    val VEX_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )

    data class Flags(
        val isAttacking: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.isAttacking) byte = byte or 0x01
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    isAttacking = (byte and 0x01) != 0,
                )
            }
        }
    }
}