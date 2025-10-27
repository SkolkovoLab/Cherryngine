package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class LlamaMeta : ChestedHorseMeta() {
    companion object : LlamaMeta()

    val STRENGTH = index(MetadataEntry.Type.VAR_INT, 0)
    val VARIANT = index<Int, Variant>(
        MetadataEntry.Type.VAR_INT,
        Variant.CREAMY,
        ::fromIndex,
        ::intIndex
    )

    enum class Variant {
        CREAMY,
        WHITE,
        BROWN,
        GRAY
    }
}