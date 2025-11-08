package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.CowVariant

object CowVariants {
    val COLD = RegistryKey<CowVariant>(Key.key("minecraft:cold"))
    val TEMPERATE = RegistryKey<CowVariant>(Key.key("minecraft:temperate"))
    val WARM = RegistryKey<CowVariant>(Key.key("minecraft:warm"))
}
