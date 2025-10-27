package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.CowVariants

@Suppress("PropertyName")
sealed class CowMeta : AgeableMobMeta() {
    companion object : CowMeta()

    val VARIANT = index(MetadataEntry.Type.COW_VARIANT, CowVariants.TEMPERATE)
}