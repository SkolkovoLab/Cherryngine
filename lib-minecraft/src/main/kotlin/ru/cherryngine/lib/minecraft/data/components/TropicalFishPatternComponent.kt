package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.TropicalFishMeta
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class TropicalFishPatternComponent(
    val pattern: TropicalFishMeta.Pattern,
) : DataComponent() {

    companion object {
        val CODEC = Codec.enum<TropicalFishMeta.Pattern>().transform(
            ::TropicalFishPatternComponent,
            TropicalFishPatternComponent::pattern
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<TropicalFishMeta.Pattern>(), TropicalFishPatternComponent::pattern,
            ::TropicalFishPatternComponent
        )
    }
}