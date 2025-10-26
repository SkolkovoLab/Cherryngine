package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class SpiderMetaFlags(
    val isClimbing: Boolean = false,
) {
    companion object {
        val DEFAULT = SpiderMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, SpiderMetaFlags::isClimbing,
            ::SpiderMetaFlags
        )

        fun metaEntry(value: SpiderMetaFlags): Metadata.Entry<SpiderMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}