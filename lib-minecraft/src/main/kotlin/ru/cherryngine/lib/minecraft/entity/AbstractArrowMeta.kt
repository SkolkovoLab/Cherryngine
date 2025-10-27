package ru.cherryngine.lib.minecraft.entity


@Suppress("PropertyName")
sealed class AbstractArrowMeta : EntityMeta() {
    companion object : AbstractArrowMeta()

    val ARROW_FLAGS = index(
            MetadataEntry.Type.BYTE,
            Flags.DEFAULT,
            Flags.Companion::fromByte,
            Flags.Companion::toByte
        )
    val PIERCING_LEVEL = index(MetadataEntry.Type.BYTE, 0)
    val IN_GROUND = index(MetadataEntry.Type.BOOLEAN, false)

    data class Flags(
        val isCritical: Boolean = false,
        val isNoclip: Boolean = false, // used by loyalty tridents when returning
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.isCritical) byte = byte or 0x01
                if (flags.isNoclip) byte = byte or 0x02
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    isCritical = (byte and 0x01) != 0,
                    isNoclip = (byte and 0x02) != 0,
                )
            }
        }
    }
}