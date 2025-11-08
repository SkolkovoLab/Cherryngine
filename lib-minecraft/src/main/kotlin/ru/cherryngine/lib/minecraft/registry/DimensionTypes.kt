package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType

object DimensionTypes {
    val OVERWORLD = RegistryKey<DimensionType>(Key.key("minecraft:overworld"))
    val OVERWORLD_CAVES = RegistryKey<DimensionType>(Key.key("minecraft:overworld_caves"))
    val THE_END = RegistryKey<DimensionType>(Key.key("minecraft:the_end"))
    val NETHER = RegistryKey<DimensionType>(Key.key("minecraft:the_nether"))
}



