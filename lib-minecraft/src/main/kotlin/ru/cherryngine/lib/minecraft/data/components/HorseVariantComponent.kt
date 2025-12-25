package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.HorseMeta
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class HorseVariantComponent(
    val variant: HorseMeta.Variant,
) : DataComponent() {
    companion object {
        val CODEC = Codec.enum<HorseMeta.Variant>().transform(
            ::HorseVariantComponent,
            HorseVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<HorseMeta.Variant>(), HorseVariantComponent::variant,
            ::HorseVariantComponent
        )
    }
}