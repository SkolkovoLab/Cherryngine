package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class MobMeta : LivingEntityMeta() {
    companion object : MobMeta()

    val MOB_FLAGS =
        index(MetadataEntry.Type.BYTE, Flags.DEFAULT, Flags::fromByte, Flags::toByte)

    data class Flags(
        val noAi: Boolean = false,
        val isLeftHanded: Boolean = false,
        val isAggressive: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.noAi) byte = byte or 0x01
                if (flags.isLeftHanded) byte = byte or 0x02
                if (flags.isAggressive) byte = byte or 0x04
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    noAi = (byte and 0x01) != 0,
                    isLeftHanded = (byte and 0x02) != 0,
                    isAggressive = (byte and 0x04) != 0,
                )
            }
        }
    }
}