package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class DamageComponent(
    val damage: Int,
) : DataComponent(true) {

    companion object {
        val CODEC = Codec.INT.transform(
            ::DamageComponent,
            DamageComponent::damage
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, DamageComponent::damage,
            ::DamageComponent
        )
    }
}