package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.ChickenVariants

@Suppress("PropertyName")
sealed class ChickenMeta : AgeableMobMeta() {
    companion object : ChickenMeta()

    val VARIANT = index(MetadataEntry.Type.CHICKEN_VARIANT, ChickenVariants.TEMPERATE)
}