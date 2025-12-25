package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.AxolotlMeta
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class AxolotlVariantComponent(
    val variant: AxolotlMeta.Variant,
) : DataComponent() {
    companion object {
        val CODEC = Codec.enum<AxolotlMeta.Variant>().transform(
            ::AxolotlVariantComponent,
            AxolotlVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<AxolotlMeta.Variant>(), AxolotlVariantComponent::variant,
            ::AxolotlVariantComponent
        )
    }
}