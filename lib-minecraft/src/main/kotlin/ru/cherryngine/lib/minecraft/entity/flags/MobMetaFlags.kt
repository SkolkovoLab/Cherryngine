package ru.cherryngine.lib.minecraft.entity.flags

data class MobMetaFlags(
    val noAi: Boolean = false,
    val isLeftHanded: Boolean = false,
    val isAggressive: Boolean = false,
) {
    companion object {
        val DEFAULT = MobMetaFlags()

        fun toByte(flags: MobMetaFlags): Byte {
            var byte = 0
            if (flags.noAi) byte = byte or 0x01
            if (flags.isLeftHanded) byte = byte or 0x02
            if (flags.isAggressive) byte = byte or 0x04
            return byte.toByte()
        }

        fun fromByte(byte: Byte): MobMetaFlags {
            val byte = byte.toInt()
            return MobMetaFlags(
                noAi = (byte and 0x01) != 0,
                isLeftHanded = (byte and 0x02) != 0,
                isAggressive = (byte and 0x04) != 0,
            )
        }
    }
}