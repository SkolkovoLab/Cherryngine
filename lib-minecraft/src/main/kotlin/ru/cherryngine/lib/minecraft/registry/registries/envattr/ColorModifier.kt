package ru.cherryngine.lib.minecraft.registry.registries.envattr

import net.kyori.adventure.util.ARGBLike
import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.EitherCodec
import ru.cherryngine.lib.minecraft.tide.types.Either
import ru.cherryngine.lib.minecraft.utils.color.ARGBLikeImpl
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl
import ru.cherryngine.lib.minecraft.utils.color.argbLikeOf
import kotlin.math.max
import kotlin.math.min

interface ColorModifier<Arg> : Modifier<RGBLike, Arg> {

    companion object {
        val MAYBE_ARGB_CODEC: Codec<RGBLike> =
            EitherCodec(ARGBLikeImpl.STRING_CODEC, RGBLikeImpl.STRING_CODEC)
                .transform(
                    { either ->
                        either.unify({ it }, { it })
                    },
                    { color ->
                        if (color is ARGBLike && color.alpha() != 255)
                            Either.Left(color)
                        else
                            Either.Right(color)
                    }
                )

        val ALPHA_BLEND: ColorModifier<ARGBLike> =
            object : ColorModifier<ARGBLike> {
                override fun modify(subject: RGBLike, argument: ARGBLike): RGBLike {
                    throw UnsupportedOperationException("alpha blend is not implemented yet")
                }

                override fun argumentCodec(): Codec<ARGBLike> =
                    ARGBLikeImpl.STRING_CODEC
            }

        val ADD: ColorModifier<RGBLike> =
            object : ColorModifier<RGBLike> {
                override fun modify(subject: RGBLike, argument: RGBLike): RGBLike {
                    val alpha = (subject as? ARGBLike)?.alpha() ?: 255
                    return argbLikeOf(
                        alpha,
                        min(255, subject.red() + argument.red()),
                        min(255, subject.green() + argument.green()),
                        min(255, subject.blue() + argument.blue())
                    )
                }

                override fun argumentCodec(): Codec<RGBLike> =
                    MAYBE_ARGB_CODEC
            }

        val SUBTRACT: ColorModifier<RGBLike> =
            object : ColorModifier<RGBLike> {
                override fun modify(subject: RGBLike, argument: RGBLike): RGBLike {
                    val alpha = (subject as? ARGBLike)?.alpha() ?: 255
                    return argbLikeOf(
                        alpha,
                        max(0, subject.red() - argument.red()),
                        max(0, subject.green() - argument.green()),
                        max(0, subject.blue() - argument.blue())
                    )
                }

                override fun argumentCodec(): Codec<RGBLike> =
                    MAYBE_ARGB_CODEC
            }

        val MULTIPLY_RGB: ColorModifier<RGBLike> =
            object : ColorModifier<RGBLike> {
                override fun modify(subject: RGBLike, argument: RGBLike): RGBLike {
                    val subA = (subject as? ARGBLike)?.alpha() ?: 255
                    val argA = (argument as? ARGBLike)?.alpha() ?: 255
                    return argbLikeOf(
                        (subA * argA) / 255,
                        (subject.red() * argument.red()) / 255,
                        (subject.green() * argument.green()) / 255,
                        (subject.blue() * argument.blue()) / 255,
                    )
                }

                override fun argumentCodec(): Codec<RGBLike> =
                    RGBLikeImpl.STRING_CODEC
            }

        val MULTIPLY_ARGB: ColorModifier<ARGBLike> =
            object : ColorModifier<ARGBLike> {
                override fun modify(subject: RGBLike, argument: ARGBLike): RGBLike {
                    val subA = (subject as? ARGBLike)?.alpha() ?: 255
                    val argA = argument.alpha()
                    return argbLikeOf(
                        (subA * argA) / 255,
                        (subject.red() * argument.red()) / 255,
                        (subject.green() * argument.green()) / 255,
                        (subject.blue() * argument.blue()) / 255,
                    )
                }

                override fun argumentCodec(): Codec<ARGBLike> =
                    ARGBLikeImpl.STRING_CODEC
            }

        val BLEND_TO_GRAY: ColorModifier<BlendToGray> =
            object : ColorModifier<BlendToGray> {
                override fun modify(subject: RGBLike, argument: BlendToGray): RGBLike {
                    throw UnsupportedOperationException("blend to gray is not implemented yet")
                }

                override fun argumentCodec(): Codec<BlendToGray> =
                    BlendToGray.CODEC
            }
    }
}