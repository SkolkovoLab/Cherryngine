package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class PiglinMeta : BasePiglinMeta() {
    companion object : PiglinMeta()

    val IS_BABY = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_CHARGING_CROSSBOW = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_DANCING = index(MetadataEntry.Type.BOOLEAN, false)
}