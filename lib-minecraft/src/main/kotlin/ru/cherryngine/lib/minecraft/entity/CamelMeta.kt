package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class CamelMeta : AbstractHorse() {
    companion object : CamelMeta()

    val DASHING = index(MetadataEntry.Type.BOOLEAN, false)
    val LAST_POSE_CHANGE_TICK = index(MetadataEntry.Type.VAR_LONG, 0L)
}