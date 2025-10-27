package ru.cherryngine.lib.minecraft.entity

sealed class ParrotMeta : TameableAnimalMeta() {
    companion object : ParrotMeta()

    val VARIANT = index<Int, Variant>(
        MetadataEntry.Type.VAR_INT,
        Variant.RED_BLUE,
        ::fromIndex,
        ::intIndex
    )

    enum class Variant {
        RED_BLUE,
        BLUE,
        GREEN,
        YELLOW_BLUE,
        GRAY;
    }
}