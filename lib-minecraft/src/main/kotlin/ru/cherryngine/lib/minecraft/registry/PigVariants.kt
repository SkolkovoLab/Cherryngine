package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.PigVariant

object PigVariants {
    val COLD = RegistryKey<PigVariant>(Key.key("minecraft:cold"))
    val TEMPERATE = RegistryKey<PigVariant>(Key.key("minecraft:temperate"))
    val WARM = RegistryKey<PigVariant>(Key.key("minecraft:warm"))
}
