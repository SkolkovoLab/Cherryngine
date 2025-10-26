package ru.cherryngine.lib.minecraft.entity.flags

data class VexMetaFlags(
    val isAttacking: Boolean = false,
) {
    companion object {
        val DEFAULT = VexMetaFlags()

        fun toByte(flags: VexMetaFlags): Byte {
            var byte = 0
            if (flags.isAttacking) byte = byte or 0x01
            return byte.toByte()
        }

        fun fromByte(byte: Byte): VexMetaFlags {
            val byte = byte.toInt()
            return VexMetaFlags(
                isAttacking = (byte and 0x01) != 0,
            )
        }
    }
}