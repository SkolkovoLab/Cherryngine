package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class WitchMeta : RaiderMeta() {
    companion object : WitchMeta()

    val IS_DRINKING_POTION = index(MetadataEntry.Type.BOOLEAN, false)
}