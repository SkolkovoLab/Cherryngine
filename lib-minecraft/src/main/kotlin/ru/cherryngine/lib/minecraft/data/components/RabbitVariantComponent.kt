package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.RabbitMeta
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class RabbitVariantComponent(
    val variant: RabbitMeta.Variant,
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<RabbitMeta.Variant>(), RabbitVariantComponent::variant,
            ::RabbitVariantComponent
        )
    }
}