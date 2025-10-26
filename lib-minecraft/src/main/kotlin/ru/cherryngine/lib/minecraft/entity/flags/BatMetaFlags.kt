package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class BatMetaFlags(
    val isHanging: Boolean = false,
) {
    companion object {
        val DEFAULT = BatMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, BatMetaFlags::isHanging,
            ::BatMetaFlags
        )

        fun metaEntry(value: BatMetaFlags): Metadata.Entry<BatMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}