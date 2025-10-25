package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.extentions.fromRGBInt
import io.github.dockyardmc.extentions.getPackedInt
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.utils.CustomColor

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