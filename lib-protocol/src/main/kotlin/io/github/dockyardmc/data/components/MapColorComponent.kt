package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.extentions.asRGB
import io.github.dockyardmc.extentions.fromRGBInt
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.utils.CustomColor

class MapColorComponent(
    val color: CustomColor
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofColor(color))
    }

    companion object {
        private val customColorStreamCodec = StreamCodec.INT.transform(
            CustomColor::fromRGBInt,
            CustomColor::asRGB
        )

        val STREAM_CODEC = StreamCodec.of(
            customColorStreamCodec, MapColorComponent::color,
            ::MapColorComponent
        )
    }
}