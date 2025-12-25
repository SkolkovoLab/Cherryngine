package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl

class FireworkExplosionComponent(
    val shape: Shape,
    val colors: List<RGBLike>,
    val fadeColors: List<RGBLike>,
    val hasTrail: Boolean,
    val hasTwinkle: Boolean
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            "shape", Codec.enum<Shape>(), FireworkExplosionComponent::shape,
            "colors", RGBLikeImpl.CODEC.list(), FireworkExplosionComponent::colors,
            "fade_colors", RGBLikeImpl.CODEC.list(), FireworkExplosionComponent::fadeColors,
            "has_trail", Codec.BOOLEAN, FireworkExplosionComponent::hasTrail,
            "has_twinkle", Codec.BOOLEAN, FireworkExplosionComponent::hasTwinkle,
            ::FireworkExplosionComponent
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Shape>(), FireworkExplosionComponent::shape,
            RGBLikeImpl.NETWORK_TYPE.list(), FireworkExplosionComponent::colors,
            RGBLikeImpl.NETWORK_TYPE.list(), FireworkExplosionComponent::fadeColors,
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