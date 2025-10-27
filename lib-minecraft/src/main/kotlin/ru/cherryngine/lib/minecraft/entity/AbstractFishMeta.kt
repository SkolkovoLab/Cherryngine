package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class AbstractFishMeta : MobMeta() {
    companion object : AbstractFishMeta()

    val FROM_BUCKET = index(MetadataEntry.Type.BOOLEAN, false)
}