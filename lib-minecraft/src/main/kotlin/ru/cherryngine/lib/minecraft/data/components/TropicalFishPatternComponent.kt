package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.entity.TropicalFishMeta
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class TropicalFishPatternComponent(
    val pattern: TropicalFishMeta.Pattern,
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofEnum(pattern))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<TropicalFishMeta.Pattern>(), TropicalFishPatternComponent::pattern,
            ::TropicalFishPatternComponent
        )
    }
}