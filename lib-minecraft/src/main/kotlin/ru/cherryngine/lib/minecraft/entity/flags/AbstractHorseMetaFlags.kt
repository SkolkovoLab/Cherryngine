package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class AbstractHorseMetaFlags(
    val isTame: Boolean = false,
    val hasBred: Boolean = false,
    val isEating: Boolean = false,
    val isRearing: Boolean = false,
    val isMouthOpen: Boolean = false,
) {
    companion object {
        val DEFAULT = AbstractHorseMetaFlags()

        val STREAM_CODEC = StreamCodec.byteFlags(
            // 0x01 unused
            0x02, AbstractHorseMetaFlags::isTame,
            // 0x04 unused (previously is saddled)
            0x08, AbstractHorseMetaFlags::hasBred,
            0x10, AbstractHorseMetaFlags::isEating,
            0x20, AbstractHorseMetaFlags::isRearing,
            0x40, AbstractHorseMetaFlags::isMouthOpen,
            ::AbstractHorseMetaFlags
        )

        fun metaEntry(value: AbstractHorseMetaFlags): Metadata.Entry<AbstractHorseMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}