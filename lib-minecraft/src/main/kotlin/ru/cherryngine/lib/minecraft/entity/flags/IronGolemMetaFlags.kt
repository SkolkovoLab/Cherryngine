package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class IronGolemMetaFlags(
    val isPlayerCreated: Boolean = false,
) {
    companion object {
        val DEFAULT = IronGolemMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, IronGolemMetaFlags::isPlayerCreated,
            ::IronGolemMetaFlags
        )

        fun metaEntry(value: IronGolemMetaFlags): Metadata.Entry<IronGolemMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}