package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class AxolotlMeta : AgeableMobMeta() {
    companion object : AxolotlMeta()

    val VARIANT = index<Int, Variant>(
        MetadataEntry.Type.VAR_INT,
        Variant.LUCY,
        ::fromIndex,
        ::intIndex
    )
    val IS_PLAYING_DEAD = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_FROM_BUCKET = index(MetadataEntry.Type.BOOLEAN, false)

    enum class Variant {
        LUCY,
        WILD,
        GOLD,
        CYAN,
        BLUE
    }
}