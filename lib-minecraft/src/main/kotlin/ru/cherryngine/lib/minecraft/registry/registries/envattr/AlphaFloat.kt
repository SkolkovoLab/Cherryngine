package ru.cherryngine.lib.minecraft.registry.registries.envattr

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.EitherCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.utils.Either

data class AlphaFloat(
    val value: Float,
    val alpha: Float = 1f,
) {
    companion object {
        private val STRUCT_CODEC: StructCodec<AlphaFloat> =
            StructCodec.of(
                "value", Codec.FLOAT, AlphaFloat::value,
                "alpha", Codec.FLOAT.default(1f), AlphaFloat::alpha,
                ::AlphaFloat
            )

        val CODEC: Codec<AlphaFloat> =
            EitherCodec(Codec.FLOAT, STRUCT_CODEC).transform(
                { either ->
                    either.unify(
                        { v -> AlphaFloat(v, 1f) },
                        { af -> af }
                    )
                },
                { af ->
                    if (af.alpha == 1f)
                        Either.Left(af.value)
                    else
                        Either.Right(af)
                }
            )
    }
}