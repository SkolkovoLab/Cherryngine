package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class AbstractVehicleMeta : EntityMeta() {
    companion object : AbstractVehicleMeta()

    val SHAKING_POWER = index(MetadataEntry.Type.VAR_INT, 0)
    val SHAKING_DIRECTION = index(MetadataEntry.Type.VAR_INT, 1)
    val SHAKING_MULTIPLIER = index(MetadataEntry.Type.FLOAT, 0f)
}