package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class TameableAnimalMeta : AgeableMobMeta() {
    companion object : TameableAnimalMeta()

    val TAMEABLE_ANIMAL_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )
    val OWNER = index(MetadataEntry.Type.OPT_UUID, null)

    data class Flags(
        val isSitting: Boolean = false,
        val isTamed: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.isSitting) byte = byte or 0x01
                if (flags.isTamed) byte = byte or 0x04
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    isSitting = (byte and 0x01) != 0,
                    isTamed = (byte and 0x04) != 0,
                )
            }
        }
    }
}