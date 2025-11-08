package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.ChickenVariant

object ChickenVariants {
    val COLD = RegistryKey<ChickenVariant>(Key.key("minecraft:cold"))
    val TEMPERATE = RegistryKey<ChickenVariant>(Key.key("minecraft:temperate"))
    val WARM = RegistryKey<ChickenVariant>(Key.key("minecraft:warm"))
}
