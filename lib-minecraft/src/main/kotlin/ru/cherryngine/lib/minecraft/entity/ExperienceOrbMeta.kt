package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class ExperienceOrbMeta : EntityMeta() {
    companion object : ExperienceOrbMeta()

    val VALUE = index(MetadataEntry.Type.VAR_INT, 0)
}