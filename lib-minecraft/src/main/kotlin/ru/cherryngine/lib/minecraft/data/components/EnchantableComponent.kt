package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class EnchantableComponent(
    val level: Int
) : DataComponent() {

    companion object {
        val CODEC = Codec.INT.transform(
            ::EnchantableComponent,
            EnchantableComponent::level
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, EnchantableComponent::level,
            ::EnchantableComponent
        )
    }
}