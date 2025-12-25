package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class PotionDurationScaleComponent(
    val duration: Float
) : DataComponent() {

    companion object {
        val CODEC = Codec.FLOAT.transform(
            ::PotionDurationScaleComponent,
            PotionDurationScaleComponent::duration
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, PotionDurationScaleComponent::duration,
            ::PotionDurationScaleComponent
        )
    }
}