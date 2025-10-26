package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ArmorStandMetaFlags(
    val isSmall: Boolean = false,
    val hasArms: Boolean = false,
    val noBasePlate: Boolean = false,
    val isMarker: Boolean = false,
) {
    companion object {
        val DEFAULT = ArmorStandMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, ArmorStandMetaFlags::isSmall,
            // 0x02 unused
            0x04, ArmorStandMetaFlags::hasArms,
            0x08, ArmorStandMetaFlags::noBasePlate,
            0x10, ArmorStandMetaFlags::isMarker,
            ::ArmorStandMetaFlags
        )

        fun metaEntry(value: ArmorStandMetaFlags): Metadata.Entry<ArmorStandMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}