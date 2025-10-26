package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class TameableAnimalMetaFlags(
    val isSitting: Boolean = false,
    val isTamed : Boolean = false,
) {
    companion object {
        val DEFAULT = TameableAnimalMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, TameableAnimalMetaFlags::isSitting,
            // 0x02 unused
            0x04, TameableAnimalMetaFlags::isTamed,
            ::TameableAnimalMetaFlags
        )

        fun metaEntry(value: TameableAnimalMetaFlags): Metadata.Entry<TameableAnimalMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}