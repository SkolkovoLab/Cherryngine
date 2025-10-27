package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.ParrotMeta
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ParrotVariantComponent(
    val parrotColor: ParrotMeta.Variant,
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<ParrotMeta.Variant>(), ParrotVariantComponent::parrotColor,
            ::ParrotVariantComponent
        )
    }
}