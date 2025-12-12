package ru.cherryngine.lib.minecraft.registry.registries.envattr

import net.kyori.adventure.util.ARGBLike
import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.tide.codec.Codec

@Suppress("UNCHECKED_CAST")
sealed interface Modifier<Sub, Arg> {

    companion object {
        val BOOLEAN_OPERATORS: Map<Operator, Modifier<Boolean, *>> = mapOf(
            Operator.AND to BooleanMod.AND,
            Operator.NAND to BooleanMod.NAND,
            Operator.OR to BooleanMod.OR,
            Operator.NOR to BooleanMod.NOR,
            Operator.XOR to BooleanMod.XOR,
            Operator.XNOR to BooleanMod.XNOR,
        )

        val FLOAT_OPERATORS: Map<Operator, Modifier<Float, *>> = mapOf(
            Operator.ALPHA_BLEND to FloatMod.ALPHA_BLEND,
            Operator.ADD to FloatMod.ADD,
            Operator.SUBTRACT to FloatMod.SUBTRACT,
            Operator.MULTIPLY to FloatMod.MULTIPLY,
            Operator.MAXIMUM to FloatMod.MAXIMUM,
            Operator.MINIMUM to FloatMod.MINIMUM,
        )

        val RGB_OPERATORS: Map<Operator, Modifier<RGBLike, *>> = mapOf(
            Operator.ALPHA_BLEND to ColorMod.ALPHA_BLEND,
            Operator.ADD to ColorMod.ADD,
            Operator.SUBTRACT to ColorMod.SUBTRACT,
            Operator.MULTIPLY to ColorMod.MULTIPLY_RGB,
            Operator.BLEND_TO_GRAY to ColorMod.BLEND_TO_GRAY,
        )

        val ARGB_OPERATORS: Map<Operator, Modifier<ARGBLike, *>> = mapOf(
            Operator.ALPHA_BLEND to ColorMod.ALPHA_BLEND as Modifier<ARGBLike, *>,
            Operator.ADD to ColorMod.ADD as Modifier<ARGBLike, *>,
            Operator.SUBTRACT to ColorMod.SUBTRACT as Modifier<ARGBLike, *>,
            Operator.MULTIPLY to ColorMod.MULTIPLY_ARGB as Modifier<ARGBLike, *>,
            Operator.BLEND_TO_GRAY to ColorMod.BLEND_TO_GRAY as Modifier<ARGBLike, *>,
        )
    }

    enum class Operator {
        OVERRIDE,
        ALPHA_BLEND,
        ADD,
        SUBTRACT,
        MULTIPLY,
        BLEND_TO_GRAY,
        MINIMUM,
        MAXIMUM,
        AND,
        NAND,
        OR,
        NOR,
        XOR,
        XNOR;

        companion object {
            val CODEC: Codec<Operator> = Codec.enum<Operator>()
        }
    }

    data class Override<Value>(
        val argumentCodecValue: Codec<Value>,
    ) : Modifier<Value, Value> {
        override fun modify(subject: Value, argument: Value): Value = argument
        override fun argumentCodec(): Codec<Value> = argumentCodecValue
    }

    object BooleanMod {
        val AND: Modifier<Boolean, Boolean> = BooleanModifier.AND
        val NAND: Modifier<Boolean, Boolean> = BooleanModifier.NAND
        val OR: Modifier<Boolean, Boolean> = BooleanModifier.OR
        val NOR: Modifier<Boolean, Boolean> = BooleanModifier.NOR
        val XOR: Modifier<Boolean, Boolean> = BooleanModifier.XOR
        val XNOR: Modifier<Boolean, Boolean> = BooleanModifier.XNOR
    }

    object FloatMod {
        val ALPHA_BLEND: Modifier<Float, AlphaFloat> = FloatModifier.ALPHA_BLEND
        val ADD: Modifier<Float, Float> = FloatModifier.ADD
        val SUBTRACT: Modifier<Float, Float> = FloatModifier.SUBTRACT
        val MULTIPLY: Modifier<Float, Float> = FloatModifier.MULTIPLY
        val MAXIMUM: Modifier<Float, Float> = FloatModifier.MAXIMUM
        val MINIMUM: Modifier<Float, Float> = FloatModifier.MINIMUM
    }

    object ColorMod {
        val ALPHA_BLEND: Modifier<RGBLike, ARGBLike> = ColorModifier.ALPHA_BLEND
        val ADD: Modifier<RGBLike, RGBLike> = ColorModifier.ADD
        val SUBTRACT: Modifier<RGBLike, RGBLike> = ColorModifier.SUBTRACT
        val MULTIPLY_RGB: Modifier<RGBLike, RGBLike> = ColorModifier.MULTIPLY_RGB
        val MULTIPLY_ARGB: Modifier<RGBLike, ARGBLike> = ColorModifier.MULTIPLY_ARGB
        val BLEND_TO_GRAY: Modifier<RGBLike, BlendToGray> = ColorModifier.BLEND_TO_GRAY
    }

    fun modify(subject: Sub, argument: Arg): Sub
    fun argumentCodec(): Codec<Arg>
}