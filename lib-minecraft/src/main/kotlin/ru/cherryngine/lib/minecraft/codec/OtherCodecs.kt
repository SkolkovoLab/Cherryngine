package ru.cherryngine.lib.minecraft.codec

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

object OtherCodecs {
    val OPTIONAL_ENTITY_ID = StreamCodec.INT.transform<Int?>(
        { to -> (to - 1).takeIf { it > -1 } }
    ) { from -> (from ?: -1) + 1 }
}