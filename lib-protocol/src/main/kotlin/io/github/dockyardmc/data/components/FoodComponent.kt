package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.tide.stream.StreamCodec

data class FoodComponent(
    val nutrition: Int,
    val saturationModifier: Float,
    val canAlwaysEat: Boolean
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            static("nutrition", CRC32CHasher.ofInt(nutrition))
            static("saturation", CRC32CHasher.ofFloat(saturationModifier))
            default("can_always_eat", false, canAlwaysEat, CRC32CHasher::ofBoolean)
        }
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, FoodComponent::nutrition,
            StreamCodec.FLOAT, FoodComponent::saturationModifier,
            StreamCodec.BOOLEAN, FoodComponent::canAlwaysEat,
            ::FoodComponent
        )
    }
}