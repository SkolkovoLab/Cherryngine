package ru.cherryngine.lib.minecraft.envattr

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class BlendToGray(
    val brightness: Float,
    val factor: Float,
) {
    companion object {
        val CODEC: Codec<BlendToGray> = StructCodec.of(
            "brightness", Codec.FLOAT, BlendToGray::brightness,
            "factor", Codec.FLOAT, BlendToGray::factor,
            ::BlendToGray
        )
    }
}