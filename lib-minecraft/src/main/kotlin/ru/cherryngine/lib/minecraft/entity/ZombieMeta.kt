package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class ZombieMeta : MobMeta() {
    companion object : ZombieMeta()

    val IS_BABY = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_BECOMING_DROWNED = index(MetadataEntry.Type.BOOLEAN, false)
}