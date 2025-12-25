package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl

class CustomModelDataComponent(
    val floats: List<Float>,
    val flags: List<Boolean>,
    val strings: List<String>,
    val colors: List<RGBLike>
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            "floats", Codec.FLOAT.list(), CustomModelDataComponent::floats,
            "flags", Codec.BOOLEAN.list(), CustomModelDataComponent::flags,
            "strings", Codec.STRING.list(), CustomModelDataComponent::strings,
            "colors", RGBLikeImpl.CODEC.list(), CustomModelDataComponent::colors,
            ::CustomModelDataComponent
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT.list(), CustomModelDataComponent::floats,
            StreamCodec.BOOLEAN.list(), CustomModelDataComponent::flags,
            StreamCodec.STRING.list(), CustomModelDataComponent::strings,
            RGBLikeImpl.NETWORK_TYPE.list(), CustomModelDataComponent::colors,
            ::CustomModelDataComponent
        )
    }
}