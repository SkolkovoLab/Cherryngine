package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class SlimeMeta : MobMeta() {
    companion object : SlimeMeta()

    val SIZE = index(MetadataEntry.Type.VAR_INT, 1)
}