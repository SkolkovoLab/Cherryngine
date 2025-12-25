package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.protocol.types.DyeColor
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class SheepColorComponent(
    val color: DyeColor
) : DataComponent() {

    companion object {
        val CODEC = Codec.enum<DyeColor>().transform(
            ::SheepColorComponent,
            SheepColorComponent::color
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<DyeColor>(), SheepColorComponent::color,
            ::SheepColorComponent
        )
    }
}