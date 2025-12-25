package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.LlamaMeta
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class LlamaVariantComponent(
    val variant: LlamaMeta.Variant,
) : DataComponent() {
    companion object {
        val CODEC = Codec.enum<LlamaMeta.Variant>().transform(
            ::LlamaVariantComponent,
            LlamaVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<LlamaMeta.Variant>(), LlamaVariantComponent::variant,
            ::LlamaVariantComponent
        )
    }
}