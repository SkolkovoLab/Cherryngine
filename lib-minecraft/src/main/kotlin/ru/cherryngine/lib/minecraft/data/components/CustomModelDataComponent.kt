package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.extentions.fromRGBInt
import ru.cherryngine.lib.minecraft.extentions.getPackedInt
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.CustomColor

class CustomModelDataComponent(
    val floats: List<Float>,
    val flags: List<Boolean>,
    val strings: List<String>,
    val colors: List<CustomColor>
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
        val customColorStreamCodec = StreamCodec.INT.transform(CustomColor::fromRGBInt, CustomColor::getPackedInt)
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT.list(), CustomModelDataComponent::floats,
            StreamCodec.BOOLEAN.list(), CustomModelDataComponent::flags,
            StreamCodec.STRING.list(), CustomModelDataComponent::strings,
            customColorStreamCodec.list(), CustomModelDataComponent::colors,
            ::CustomModelDataComponent
        )
    }
}