package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.DialogInputType

object DialogInputTypes {
    val BOOLEAN = RegistryKey<DialogInputType>(Key.key("minecraft:boolean"))
    val NUMBER_RANGE = RegistryKey<DialogInputType>(Key.key("minecraft:number_range"))
    val SINGLE_OPTION = RegistryKey<DialogInputType>(Key.key("minecraft:single_option"))
    val TEXT = RegistryKey<DialogInputType>(Key.key("minecraft:text"))
}
