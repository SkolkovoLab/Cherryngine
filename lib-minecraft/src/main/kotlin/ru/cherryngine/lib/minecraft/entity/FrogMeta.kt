package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.Registries

@Suppress("PropertyName")
sealed class FrogMeta : AgeableMobMeta() {
    companion object : FrogMeta()

    val VARIANT = index(MetadataEntry.Type.FROG_VARIANT, Registries.frogVariant.getValue("temperate"))
    val TONGUE_TARGET = index(MetadataEntry.Type.OPT_VAR_INT, 0)
}