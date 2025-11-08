package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.WolfVariant

object WolfVariants {
    val ASHEN = RegistryKey<WolfVariant>(Key.key("minecraft:ashen"))
    val BLACK = RegistryKey<WolfVariant>(Key.key("minecraft:black"))
    val CHESTNUT = RegistryKey<WolfVariant>(Key.key("minecraft:chestnut"))
    val PALE = RegistryKey<WolfVariant>(Key.key("minecraft:pale"))
    val RUSTY = RegistryKey<WolfVariant>(Key.key("minecraft:rusty"))
    val SNOWY = RegistryKey<WolfVariant>(Key.key("minecraft:snowy"))
    val SPOTTED = RegistryKey<WolfVariant>(Key.key("minecraft:spotted"))
    val STRIPED = RegistryKey<WolfVariant>(Key.key("minecraft:striped"))
    val WOODS = RegistryKey<WolfVariant>(Key.key("minecraft:woods"))
}
