package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.extentions.fromRGBInt
import io.github.dockyardmc.extentions.getPackedInt
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.utils.CustomColor

class FireworkExplosionComponent(
    val shape: Shape,
    val colors: List<CustomColor>,
    val fadeColors: List<CustomColor>,
    val hasTrail: Boolean,
    val hasTwinkle: Boolean
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
        private val customColorStreamCodec = StreamCodec.INT.transform(
            CustomColor::fromRGBInt,
            CustomColor::getPackedInt
        )

        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Shape>(), FireworkExplosionComponent::shape,
            customColorStreamCodec.list(), FireworkExplosionComponent::colors,
            customColorStreamCodec.list(), FireworkExplosionComponent::fadeColors,
            StreamCodec.BOOLEAN, FireworkExplosionComponent::hasTrail,
            StreamCodec.BOOLEAN, FireworkExplosionComponent::hasTwinkle,
            ::FireworkExplosionComponent
        )
    }

    enum class Shape {
        SMALL_BALL,
        LARGE_BALL,
        STAR,
        CREEPER,
        BURST
    }
}