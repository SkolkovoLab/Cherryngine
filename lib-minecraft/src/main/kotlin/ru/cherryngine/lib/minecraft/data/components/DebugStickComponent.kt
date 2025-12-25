package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.MapCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.MapStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class DebugStickComponent(
    val state: Map<String, String>,
) : DataComponent() {

    companion object {
        val CODEC = MapCodec(Codec.STRING, Codec.STRING).transform(
            ::DebugStickComponent,
            DebugStickComponent::state
        )
        val STREAM_CODEC = StreamCodec.of(
            MapStreamCodec(StreamCodec.STRING, StreamCodec.STRING), DebugStickComponent::state,
            ::DebugStickComponent
        )
    }
}