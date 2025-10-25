package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.extentions.fromRGBInt
import ru.cherryngine.lib.minecraft.extentions.getPackedInt
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.CustomColor

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