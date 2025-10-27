package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.protocol.types.PlayerHand

@Suppress("PropertyName")
sealed class LivingEntityMeta : EntityMeta() {
    companion object : LivingEntityMeta()

    val LIVING_ENTITY_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )
    val HEALTH = index(MetadataEntry.Type.FLOAT, 1f)
    val POTION_EFFECT_PARTICLES = index(MetadataEntry.Type.PARTICLE_LIST, listOf())
    val IS_POTION_EFFECT_AMBIANT = index(MetadataEntry.Type.BOOLEAN, false)
    val NUMBER_OF_ARROWS = index(MetadataEntry.Type.VAR_INT, 0)
    val NUMBER_OF_BEE_STINGERS = index(MetadataEntry.Type.VAR_INT, 0)
    val LOCATION_OF_BED = index(MetadataEntry.Type.OPT_BLOCK_POSITION, null)

    data class Flags(
        val isHandActive: Boolean = false,
        val activeHand: PlayerHand = PlayerHand.MAIN_HAND,
        val isInRiptideSpinAttack: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.isHandActive) byte = byte or 0x01
                if (flags.activeHand == PlayerHand.OFF_HAND) byte = byte or 0x02
                if (flags.isInRiptideSpinAttack) byte = byte or 0x04
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    isHandActive = (byte and 0x01) != 0,
                    activeHand = if ((byte and 0x02) != 0) PlayerHand.OFF_HAND else PlayerHand.MAIN_HAND,
                    isInRiptideSpinAttack = (byte and 0x04) != 0,
                )
            }
        }
    }
}