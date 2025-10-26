package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ArrowMetaFlags(
    val isCritical: Boolean = false,
    val isNoclip : Boolean = false, // used by loyalty tridents when returning
) {
    companion object {
        val DEFAULT = ArrowMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, ArrowMetaFlags::isCritical,
            0x02, ArrowMetaFlags::isNoclip,
            ::ArrowMetaFlags
        )

        fun metaEntry(value: ArrowMetaFlags): Metadata.Entry<ArrowMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}