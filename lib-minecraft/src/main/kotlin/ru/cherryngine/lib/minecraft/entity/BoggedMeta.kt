package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class BoggedMeta : MobMeta() {
    companion object : BoggedMeta()

    val IS_SHEARED = index(MetadataEntry.Type.BOOLEAN, false)
}