package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class GhastMeta : MobMeta() {
    companion object : GhastMeta()

    val IS_ATTACKING = index(MetadataEntry.Type.BOOLEAN, false)
}