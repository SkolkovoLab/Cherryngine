package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.Particles

@Suppress("PropertyName")
sealed class AreaEffectCloudMeta : EntityMeta() {
    companion object : AreaEffectCloudMeta()

    val RADIUS = index(MetadataEntry.Type.FLOAT, 0.5f)
    val COLOR = index(MetadataEntry.Type.VAR_INT, 0)
    val IGNORE_RADIUS_AND_SINGLE_POINT = index(MetadataEntry.Type.BOOLEAN, false)
    val PARTICLE = index(MetadataEntry.Type.PARTICLE, Particles.EFFECT)
}