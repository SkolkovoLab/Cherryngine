package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class WitherMeta : MobMeta() {
    companion object : WitherMeta()

    val CENTER_HEAD_TARGET = index(MetadataEntry.Type.VAR_INT, 0)
    val LEFT_HEAD_TARGET = index(MetadataEntry.Type.VAR_INT, 0)
    val RIGHT_HEAD_TARGET = index(MetadataEntry.Type.VAR_INT, 0)
    val INVULNERABLE_TIME = index(MetadataEntry.Type.VAR_INT, 0)
}