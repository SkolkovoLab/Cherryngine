package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class PandaMetaFlags(
    val isSneezing : Boolean = false,
    val isRolling: Boolean = false,
    val isSitting: Boolean = false,
    val isOnBack: Boolean = false,
) {
    companion object {
        val DEFAULT = PandaMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            // 0x01 unused
            0x02, PandaMetaFlags::isSneezing,
            0x04, PandaMetaFlags::isRolling,
            0x08, PandaMetaFlags::isSitting,
            0x10, PandaMetaFlags::isOnBack,
            ::PandaMetaFlags
        )

        fun metaEntry(value: PandaMetaFlags): Metadata.Entry<PandaMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}