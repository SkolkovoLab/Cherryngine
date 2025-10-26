package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class BeeMetaFlags(
    val isAngry: Boolean = false,
    val hasStung : Boolean = false,
    val hasNectar : Boolean = false,
) {
    companion object {
        val DEFAULT = BeeMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            // 0x01 unused
            0x02, BeeMetaFlags::isAngry,
            0x04, BeeMetaFlags::hasStung,
            0x08, BeeMetaFlags::hasNectar,
            ::BeeMetaFlags
        )

        fun metaEntry(value: BeeMetaFlags): Metadata.Entry<BeeMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}