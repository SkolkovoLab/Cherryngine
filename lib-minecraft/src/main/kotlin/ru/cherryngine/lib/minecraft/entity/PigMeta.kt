package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.Registries

@Suppress("PropertyName")
sealed class PigMeta : AgeableMobMeta() {
    companion object : PigMeta()

    val BOOST_TIME = index(MetadataEntry.Type.VAR_INT, 0)
    val VARIANT = index(MetadataEntry.Type.PIG_VARIANT, Registries.pigVariant["temperate"])
}