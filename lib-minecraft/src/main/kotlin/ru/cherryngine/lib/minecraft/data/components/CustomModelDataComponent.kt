package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl

class CustomModelDataComponent(
    val floats: List<Float>,
    val flags: List<Boolean>,
    val strings: List<String>,
    val colors: List<RGBLike>
) : DataComponent() {

    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            defaultList("floats", emptyList(), floats, CRC32CHasher::ofFloat)
            defaultList("flags", emptyList(), flags, CRC32CHasher::ofBoolean)
            defaultList("strings", emptyList(), strings, CRC32CHasher::ofString)
            defaultList("colors", emptyList(), colors, CRC32CHasher::ofColor)
        }
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT.list(), CustomModelDataComponent::floats,
            StreamCodec.BOOLEAN.list(), CustomModelDataComponent::flags,
            StreamCodec.STRING.list(), CustomModelDataComponent::strings,
            RGBLikeImpl.NETWORK_TYPE.list(), CustomModelDataComponent::colors,
            ::CustomModelDataComponent
        )
    }
}