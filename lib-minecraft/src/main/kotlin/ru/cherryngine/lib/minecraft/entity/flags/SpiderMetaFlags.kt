package ru.cherryngine.lib.minecraft.entity.flags

data class SpiderMetaFlags(
    val isClimbing: Boolean = false,
) {
    companion object {
        val DEFAULT = SpiderMetaFlags()

        fun toByte(flags: SpiderMetaFlags): Byte {
            var byte = 0
            if (flags.isClimbing) byte = byte or 0x01
            return byte.toByte()
        }

        fun fromByte(byte: Byte): SpiderMetaFlags {
            val byte = byte.toInt()
            return SpiderMetaFlags(
                isClimbing = (byte and 0x01) != 0,
            )
        }
    }
}