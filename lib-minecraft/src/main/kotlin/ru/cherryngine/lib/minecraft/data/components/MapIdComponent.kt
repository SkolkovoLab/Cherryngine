package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class MapIdComponent(
    val mapId: Int
) : DataComponent() {

    companion object {
        val CODEC = Codec.INT.transform(
            ::MapIdComponent,
            MapIdComponent::mapId
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, MapIdComponent::mapId,
            ::MapIdComponent
        )
    }
}