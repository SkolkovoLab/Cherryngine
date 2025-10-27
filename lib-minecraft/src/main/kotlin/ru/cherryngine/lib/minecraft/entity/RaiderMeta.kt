package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class RaiderMeta : MobMeta() {
    companion object : RaiderMeta()

    val IS_CELEBRATING = index(MetadataEntry.Type.BOOLEAN, false)
}