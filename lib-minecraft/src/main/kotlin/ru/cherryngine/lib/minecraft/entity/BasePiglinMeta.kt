package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class BasePiglinMeta : MobMeta() {
    companion object : BasePiglinMeta()

    val IMMUNE_ZOMBIFICATION = index(MetadataEntry.Type.BOOLEAN, false)
}