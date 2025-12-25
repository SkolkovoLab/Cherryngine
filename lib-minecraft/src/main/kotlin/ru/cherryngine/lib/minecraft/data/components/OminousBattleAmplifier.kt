package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class OminousBattleAmplifier(
    val amplifier: Int
) : DataComponent() {

    companion object {
        val CODEC = Codec.INT.transform(
            ::OminousBattleAmplifier,
            OminousBattleAmplifier::amplifier
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, OminousBattleAmplifier::amplifier,
            ::OminousBattleAmplifier
        )
    }
}