package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class FoxMetaFlags(
    val isSitting: Boolean = false,
    val isCrouching: Boolean = false,
    val isInterested: Boolean = false,
    val isPouncing: Boolean = false,
    val isSleeping: Boolean = false,
    val isFaceplanted: Boolean = false,
    val isDefending: Boolean = false,
) {
    companion object {
        val DEFAULT = FoxMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, FoxMetaFlags::isSitting,
            // 0x02 unused
            0x04, FoxMetaFlags::isCrouching,
            0x08, FoxMetaFlags::isInterested,
            0x10, FoxMetaFlags::isPouncing,
            0x20, FoxMetaFlags::isSleeping,
            0x40, FoxMetaFlags::isFaceplanted,
            0x80, FoxMetaFlags::isDefending,
            ::FoxMetaFlags
        )

        fun metaEntry(value: FoxMetaFlags): Metadata.Entry<FoxMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}