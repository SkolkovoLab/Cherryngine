package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class TurtleMeta : AgeableMobMeta() {
    companion object : TurtleMeta()

    val HAS_EGG = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_LAYING_EGG = index(MetadataEntry.Type.BOOLEAN, false)
}