package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.r2.Registries

@Suppress("PropertyName")
sealed class CowMeta : AgeableMobMeta() {
    companion object : CowMeta()

    val VARIANT = index(MetadataEntry.Type.COW_VARIANT, Registries.cowVariant["temperate"])
}