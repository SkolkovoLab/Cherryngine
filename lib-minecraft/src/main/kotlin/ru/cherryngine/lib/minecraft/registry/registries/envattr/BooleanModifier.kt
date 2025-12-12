package ru.cherryngine.lib.minecraft.registry.registries.envattr

import ru.cherryngine.lib.minecraft.codec.Codec

fun interface BooleanModifier : Modifier<Boolean, Boolean> {

    override fun modify(subject: Boolean, argument: Boolean): Boolean

    override fun argumentCodec(): Codec<Boolean> = Codec.BOOLEAN

    companion object {
        val AND = BooleanModifier { a, b -> a && b }
        val NAND = BooleanModifier { a, b -> !a || !b }
        val OR = BooleanModifier { a, b -> a || b }
        val NOR = BooleanModifier { a, b -> !a && !b }
        val XOR = BooleanModifier { a, b -> a.xor(b) }
        val XNOR = BooleanModifier { a, b -> a == b }
    }
}