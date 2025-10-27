package ru.cherryngine.lib.minecraft.entity

sealed class IronGolemMeta : MobMeta() {
    companion object : IronGolemMeta()

    val IRON_GOLEM_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )

    data class Flags(
        val isPlayerCreated: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.isPlayerCreated) byte = byte or 0x01
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    isPlayerCreated = (byte and 0x01) != 0,
                )
            }
        }
    }
}