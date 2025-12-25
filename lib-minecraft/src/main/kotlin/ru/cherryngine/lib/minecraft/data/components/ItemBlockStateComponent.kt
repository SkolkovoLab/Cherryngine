package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.MapCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.MapStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ItemBlockStateComponent(
    val properties: Map<String, String>
) : DataComponent() {

    companion object {
        val CODEC = MapCodec(Codec.STRING, Codec.STRING).transform(
            ::ItemBlockStateComponent,
            ItemBlockStateComponent::properties
        )
        val STREAM_CODEC = StreamCodec.of(
            MapStreamCodec(StreamCodec.STRING, StreamCodec.STRING), ItemBlockStateComponent::properties,
            ::ItemBlockStateComponent
        )
    }
}