package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.keys.FrogVariants

@Suppress("PropertyName")
sealed class FrogMeta : AgeableMobMeta() {
    companion object : FrogMeta()

    val VARIANT = index(MetadataEntry.Type.FROG_VARIANT, Registries.frogVariant[FrogVariants.TEMPERATE].value)
    val TONGUE_TARGET = index(MetadataEntry.Type.OPT_VAR_INT, 0)
}