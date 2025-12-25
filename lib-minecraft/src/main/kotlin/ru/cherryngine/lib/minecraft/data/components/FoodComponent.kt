package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class FoodComponent(
    val nutrition: Int,
    val saturationModifier: Float,
    val canAlwaysEat: Boolean
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            "nutrition", Codec.INT, FoodComponent::nutrition,
            "saturation", Codec.FLOAT, FoodComponent::saturationModifier,
            "can_always_eat", Codec.BOOLEAN.default(false), FoodComponent::canAlwaysEat,
            ::FoodComponent
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, FoodComponent::nutrition,
            StreamCodec.FLOAT, FoodComponent::saturationModifier,
            StreamCodec.BOOLEAN, FoodComponent::canAlwaysEat,
            ::FoodComponent
        )
    }
}