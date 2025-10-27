package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class ZoglinMeta : MobMeta() {
    companion object : ZoglinMeta()

    val IS_BABY = index(MetadataEntry.Type.BOOLEAN, false)
}