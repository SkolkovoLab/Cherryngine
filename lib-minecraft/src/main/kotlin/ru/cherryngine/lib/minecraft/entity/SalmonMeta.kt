package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class SalmonMeta : AbstractFishMeta() {
    companion object : SalmonMeta()

    val SIZE = index<Int, Size>(
        MetadataEntry.Type.VAR_INT,
        Size.SMALL,
        ::fromIndex,
        ::intIndex
    )

    enum class Size {
        SMALL,
        MEDIUM,
        LARGE
    }
}