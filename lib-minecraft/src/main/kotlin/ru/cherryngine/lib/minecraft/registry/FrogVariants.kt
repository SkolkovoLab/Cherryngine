package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.FrogVariant

object FrogVariants {
    val COLD = RegistryKey<FrogVariant>(Key.key("minecraft:cold"))
    val TEMPERATE = RegistryKey<FrogVariant>(Key.key("minecraft:temperate"))
    val WARM = RegistryKey<FrogVariant>(Key.key("minecraft:warm"))
}
