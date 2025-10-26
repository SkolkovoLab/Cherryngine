package ru.cherryngine.lib.minecraft.entity.flags

data class IronGolemMetaFlags(
    val isPlayerCreated: Boolean = false,
) {
    companion object {
        val DEFAULT = IronGolemMetaFlags()

        fun toByte(flags: IronGolemMetaFlags): Byte {
            var byte = 0
            if (flags.isPlayerCreated) byte = byte or 0x01
            return byte.toByte()
        }

        fun fromByte(byte: Byte): IronGolemMetaFlags {
            val byte = byte.toInt()
            return IronGolemMetaFlags(
                isPlayerCreated = (byte and 0x01) != 0,
            )
        }
    }
}