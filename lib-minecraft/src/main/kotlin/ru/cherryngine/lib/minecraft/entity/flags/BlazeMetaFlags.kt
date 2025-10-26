package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class BlazeMetaFlags(
    val isOnFire: Boolean = false,
) {
    companion object {
        val DEFAULT = BlazeMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, BlazeMetaFlags::isOnFire,
            ::BlazeMetaFlags
        )

        fun metaEntry(value: BlazeMetaFlags): Metadata.Entry<BlazeMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}