package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.keys.ZombieNautilusVariants

@Suppress("PropertyName")
sealed class ZombieNautilusMeta : AbstractNautilusMeta() {
    companion object : ZombieNautilusMeta()

    val VARIANT = index(MetadataEntry.Type.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariants.TEMPERATE)
}