package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.MapCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.MapStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class MapDecorationsComponent(
    val decorations: Map<String, Decoration>
) : DataComponent() {
    companion object {
        val CODEC = MapCodec(Codec.STRING, Decoration.CODEC).transform(
            ::MapDecorationsComponent,
            MapDecorationsComponent::decorations
        )
        val STREAM_CODEC = StreamCodec.of(
            MapStreamCodec(StreamCodec.STRING, Decoration.STREAM_CODEC), MapDecorationsComponent::decorations,
            ::MapDecorationsComponent
        )
    }

    data class Decoration(
        val type: String,
        val x: Double,
        val z: Double,
        val rotation: Float
    ) {
        companion object {
            val CODEC = StructCodec.of(
                "type", Codec.STRING, Decoration::type,
                "x", Codec.DOUBLE, Decoration::x,
                "z", Codec.DOUBLE, Decoration::z,
                "rotation", Codec.FLOAT, Decoration::rotation,
                ::Decoration
            )
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, Decoration::type,
                StreamCodec.DOUBLE, Decoration::x,
                StreamCodec.DOUBLE, Decoration::z,
                StreamCodec.FLOAT, Decoration::rotation,
                ::Decoration
            )
        }
    }
}