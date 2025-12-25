package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class TooltipStyleComponent(
    val style: String
) : DataComponent() {

    companion object {
        val CODEC = Codec.STRING.transform(
            ::TooltipStyleComponent,
            TooltipStyleComponent::style
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, TooltipStyleComponent::style,
            ::TooltipStyleComponent
        )
    }
}