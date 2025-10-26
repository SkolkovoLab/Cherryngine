package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.protocol.types.PlayerHand

data class LivingEntityMetaFlags(
    val isHandActive: Boolean = false,
    val activeHand: PlayerHand = PlayerHand.MAIN_HAND,
    val isInRiptideSpinAttack: Boolean = false,
) {
    companion object {
        val DEFAULT = LivingEntityMetaFlags()

        fun toByte(flags: LivingEntityMetaFlags): Byte {
            var byte = 0
            if (flags.isHandActive) byte = byte or 0x01
            if (flags.activeHand == PlayerHand.OFF_HAND) byte = byte or 0x02
            if (flags.isInRiptideSpinAttack) byte = byte or 0x04
            return byte.toByte()
        }

        fun fromByte(byte: Byte): LivingEntityMetaFlags {
            val byte = byte.toInt()
            return LivingEntityMetaFlags(
                isHandActive = (byte and 0x01) != 0,
                activeHand = if ((byte and 0x02) != 0) PlayerHand.OFF_HAND else PlayerHand.MAIN_HAND,
                isInRiptideSpinAttack = (byte and 0x04) != 0,
            )
        }
    }
}