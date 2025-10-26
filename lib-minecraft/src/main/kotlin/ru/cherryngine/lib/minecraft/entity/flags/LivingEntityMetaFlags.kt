package ru.cherryngine.lib.minecraft.entity.types

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.protocol.types.PlayerHand
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class LivingEntityMetaFlags(
    val isHandActive: Boolean = false,
    val activeHand: PlayerHand = PlayerHand.MAIN_HAND,
    val isInRiptideSpinAttack: Boolean = false,
) {
    companion object {
        val DEFAULT = LivingEntityMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, LivingEntityMetaFlags::isHandActive,
            0x02, { it.activeHand == PlayerHand.OFF_HAND },
            0x04, LivingEntityMetaFlags::isInRiptideSpinAttack
        ) { isHandActive, activeHand, isInRiptideSpinAttack ->
            LivingEntityMetaFlags(
                isHandActive,
                if (activeHand) PlayerHand.OFF_HAND else PlayerHand.MAIN_HAND,
                isInRiptideSpinAttack
            )
        }

        fun metaEntry(value: LivingEntityMetaFlags): Metadata.Entry<LivingEntityMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}