package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class MapPostProcessing(
    val type: Type
) : DataComponent() {

    companion object {
        val CODEC = Codec.enum<Type>().transform(
            ::MapPostProcessing,
            MapPostProcessing::type
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Type>(), MapPostProcessing::type,
            ::MapPostProcessing
        )
    }

    enum class Type {
        LOCK,
        SCALE
    }
}