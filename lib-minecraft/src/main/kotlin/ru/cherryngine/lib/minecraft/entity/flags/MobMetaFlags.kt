package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class MobMetaFlags(
    val noAi: Boolean = false,
    val isLeftHanded: Boolean = false,
    val isAggressive: Boolean = false,
) {
    companion object {
        val DEFAULT = MobMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, MobMetaFlags::noAi,
            0x02, MobMetaFlags::isLeftHanded,
            0x04, MobMetaFlags::isAggressive,
            ::MobMetaFlags
        )

        fun metaEntry(value: MobMetaFlags): Metadata.Entry<MobMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}