package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
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
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
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