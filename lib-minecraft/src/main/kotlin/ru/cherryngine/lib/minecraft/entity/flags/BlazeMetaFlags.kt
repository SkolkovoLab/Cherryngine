package ru.cherryngine.lib.minecraft.entity.flags

data class BlazeMetaFlags(
    val isOnFire: Boolean = false,
) {
    companion object {
        val DEFAULT = BlazeMetaFlags()

        fun toByte(flags: BlazeMetaFlags): Byte {
            var byte = 0
            if (flags.isOnFire) byte = byte or 0x01
            return byte.toByte()
        }

        fun fromByte(byte: Byte): BlazeMetaFlags {
            val byte = byte.toInt()
            return BlazeMetaFlags(
                isOnFire = (byte and 0x01) != 0,
            )
        }
    }
}