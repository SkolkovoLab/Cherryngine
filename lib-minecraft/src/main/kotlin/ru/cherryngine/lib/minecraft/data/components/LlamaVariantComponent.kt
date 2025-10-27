package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.LlamaMeta
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class LlamaVariantComponent(
    val variant: LlamaMeta.Variant,
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<LlamaMeta.Variant>(), LlamaVariantComponent::variant,
            ::LlamaVariantComponent
        )
    }
}