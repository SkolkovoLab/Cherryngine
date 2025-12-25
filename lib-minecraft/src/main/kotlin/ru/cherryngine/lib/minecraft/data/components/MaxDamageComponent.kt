package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class MaxDamageComponent(
    val maxDamage: Int,
) : DataComponent() {

    companion object {
        val CODEC = Codec.INT.transform(
            ::MaxDamageComponent,
            MaxDamageComponent::maxDamage
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, MaxDamageComponent::maxDamage,
            ::MaxDamageComponent
        )
    }
}