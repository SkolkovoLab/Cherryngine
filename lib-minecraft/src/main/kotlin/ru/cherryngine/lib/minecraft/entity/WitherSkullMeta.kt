package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class WitherSkullMeta : EntityMeta() {
    companion object : WitherSkullMeta()

    val IS_INVULNERABLE = index(MetadataEntry.Type.BOOLEAN, false)
}