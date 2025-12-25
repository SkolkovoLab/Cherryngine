package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class MaxStackSizeComponent(
    val size: Int,
) : DataComponent(true) {

    companion object {
        val CODEC = Codec.INT.transform(::MaxStackSizeComponent, MaxStackSizeComponent::size)
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, MaxStackSizeComponent::size,
            ::MaxStackSizeComponent
        )
    }
}