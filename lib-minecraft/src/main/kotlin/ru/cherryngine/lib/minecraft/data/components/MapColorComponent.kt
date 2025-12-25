package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl

class MapColorComponent(
    val color: RGBLike
) : DataComponent() {

    companion object {
        val CODEC = RGBLikeImpl.CODEC.transform(
            ::MapColorComponent,
            MapColorComponent::color
        )
        val STREAM_CODEC = StreamCodec.of(
            RGBLikeImpl.NETWORK_TYPE, MapColorComponent::color,
            ::MapColorComponent
        )
    }
}