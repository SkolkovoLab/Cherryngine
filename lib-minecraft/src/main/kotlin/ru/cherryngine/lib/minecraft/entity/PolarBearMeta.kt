package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class PolarBearMeta : AgeableMobMeta() {
    companion object : PolarBearMeta()

    val IS_STANDING_UP = index(MetadataEntry.Type.BOOLEAN, false)
}