package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class VexMetaFlags(
    val isAttacking: Boolean = false,
) {
    companion object {
        val DEFAULT = VexMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, VexMetaFlags::isAttacking,
            ::VexMetaFlags
        )

        fun metaEntry(value: VexMetaFlags): Metadata.Entry<VexMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}