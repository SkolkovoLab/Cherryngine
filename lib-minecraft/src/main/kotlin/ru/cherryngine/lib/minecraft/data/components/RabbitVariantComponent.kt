package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.RabbitMeta
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class RabbitVariantComponent(
    val variant: RabbitMeta.Variant,
) : DataComponent() {
    companion object {
        val CODEC = Codec.enum<RabbitMeta.Variant>().transform(
            ::RabbitVariantComponent,
            RabbitVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<RabbitMeta.Variant>(), RabbitVariantComponent::variant,
            ::RabbitVariantComponent
        )
    }
}