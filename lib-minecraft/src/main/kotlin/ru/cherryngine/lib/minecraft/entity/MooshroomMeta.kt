package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class MooshroomMeta : AgeableMobMeta() {
    companion object : MooshroomMeta()

    val VARIANT = index<Int, Variant>(
        MetadataEntry.Type.VAR_INT,
        Variant.RED,
        ::fromIndex,
        ::intIndex
    )

    enum class Variant {
        RED,
        BROWN
    }
}