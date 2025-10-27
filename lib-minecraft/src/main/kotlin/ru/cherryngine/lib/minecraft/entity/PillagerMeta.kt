package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class PillagerMeta : RaiderMeta() {
    companion object : PillagerMeta()

    val IS_CHARGING = index(MetadataEntry.Type.BOOLEAN, false)
}