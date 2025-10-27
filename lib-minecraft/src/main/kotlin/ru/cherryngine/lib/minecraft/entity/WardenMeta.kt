package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class WardenMeta : MobMeta() {
    companion object : WardenMeta()

    val ANGER_LEVEL = index(MetadataEntry.Type.VAR_INT, 0)
}