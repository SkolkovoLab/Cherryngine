package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class EnchantmentGlintOverrideComponent(
    val enchantGlint: Boolean
) : DataComponent() {

    companion object {
        val CODEC = Codec.BOOLEAN.transform(
            ::EnchantmentGlintOverrideComponent,
            EnchantmentGlintOverrideComponent::enchantGlint
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BOOLEAN, EnchantmentGlintOverrideComponent::enchantGlint,
            ::EnchantmentGlintOverrideComponent
        )
    }
}