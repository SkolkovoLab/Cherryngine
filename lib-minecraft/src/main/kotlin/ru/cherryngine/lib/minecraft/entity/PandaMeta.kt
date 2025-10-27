package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class PandaMeta : AgeableMobMeta() {
    companion object : PandaMeta()

    val BREED_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
    val SNEEZE_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
    val EAT_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
    val MAIN_GENE = index<Byte, Gene>(
        MetadataEntry.Type.BYTE,
        Gene.NORMAL,
        ::fromIndex,
        ::byteIndex
    )
    val HIDDEN_GENE = index<Byte, Gene>(
        MetadataEntry.Type.BYTE,
        Gene.NORMAL,
        ::fromIndex,
        ::byteIndex
    )
    val PANDA_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )

    data class Flags(
        val isSneezing: Boolean = false,
        val isRolling: Boolean = false,
        val isSitting: Boolean = false,
        val isOnBack: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.isSneezing) byte = byte or 0x02
                if (flags.isRolling) byte = byte or 0x04
                if (flags.isSitting) byte = byte or 0x08
                if (flags.isOnBack) byte = byte or 0x10
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    isSneezing = (byte and 0x02) != 0,
                    isRolling = (byte and 0x04) != 0,
                    isSitting = (byte and 0x08) != 0,
                    isOnBack = (byte and 0x10) != 0,
                )
            }
        }
    }

    enum class Gene {
        NORMAL,
        LAZY,
        WORRIED,
        PLAYFUL,
        BROWN,
        WEAK,
        AGGRESSIVE
    }
}