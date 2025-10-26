package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class SnowGolemMetaFlags(
    val hasPumpkin: Boolean = true,
) {
    companion object {
        val DEFAULT = SnowGolemMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            // 0x01 unused
            // 0x02 unused
            // 0x04 unused
            // 0x08 unused
            0x10, SnowGolemMetaFlags::hasPumpkin,
            ::SnowGolemMetaFlags
        )

        fun metaEntry(value: SnowGolemMetaFlags): Metadata.Entry<SnowGolemMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}