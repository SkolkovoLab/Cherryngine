package ru.cherryngine.lib.minecraft.registry.registries.envattr

import ru.cherryngine.lib.minecraft.tide.codec.Codec
import kotlin.math.max
import kotlin.math.min

interface FloatModifier<Arg> : Modifier<Float, Arg> {
    companion object {
        val ALPHA_BLEND: FloatModifier<AlphaFloat> =
            object : FloatModifier<AlphaFloat> {
                override fun modify(subject: Float, argument: AlphaFloat): Float {
                    return subject + argument.alpha * (argument.value - subject)
                }

                override fun argumentCodec(): Codec<AlphaFloat> =
                    AlphaFloat.CODEC
            }

        val ADD: ToFloat = ToFloat { x, y -> x + y }
        val SUBTRACT: ToFloat = ToFloat { x, y -> x - y }
        val MULTIPLY: ToFloat = ToFloat { x, y -> x * y }
        val MINIMUM: ToFloat = ToFloat { x, y -> min(x, y) }
        val MAXIMUM: ToFloat = ToFloat { x, y -> max(x, y) }
    }

    fun interface ToFloat : FloatModifier<Float> {
        override fun modify(subject: Float, argument: Float): Float

        override fun argumentCodec(): Codec<Float> = Codec.FLOAT
    }
}