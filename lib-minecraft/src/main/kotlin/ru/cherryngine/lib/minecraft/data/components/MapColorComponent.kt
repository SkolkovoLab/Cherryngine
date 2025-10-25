package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.extentions.asRGB
import ru.cherryngine.lib.minecraft.extentions.fromRGBInt
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.CustomColor

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