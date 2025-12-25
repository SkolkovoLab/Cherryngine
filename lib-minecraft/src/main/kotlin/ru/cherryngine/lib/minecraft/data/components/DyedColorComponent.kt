package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl

class DyedColorComponent(
    val color: RGBLike
) : DataComponent() {

    companion object {
        val CODEC = RGBLikeImpl.CODEC.transform(
            ::DyedColorComponent,
            DyedColorComponent::color
        )
        val STREAM_CODEC = StreamCodec.of(
            RGBLikeImpl.NETWORK_TYPE, DyedColorComponent::color,
            ::DyedColorComponent
        )
    }
}