package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class FishingHookMeta : EntityMeta() {
    companion object : FishingHookMeta()

    val HOOKED = index(MetadataEntry.Type.VAR_INT, 0)
    val IS_CATCHABLE = index(MetadataEntry.Type.BOOLEAN, false)
}