package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.FrogVariants

@Suppress("PropertyName")
sealed class FrogMeta : AgeableMobMeta() {
    companion object : FrogMeta()

    val VARIANT = index(MetadataEntry.Type.FROG_VARIANT, FrogVariants.TEMPERATE)
    val TONGUE_TARGET = index(MetadataEntry.Type.OPT_VAR_INT, 0)
}