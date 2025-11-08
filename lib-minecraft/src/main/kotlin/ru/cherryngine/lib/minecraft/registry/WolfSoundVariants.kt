package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.WolfSoundVariant

object WolfSoundVariants {
    val ANGRY = RegistryKey<WolfSoundVariant>(Key.key("minecraft:angry"))
    val BIG = RegistryKey<WolfSoundVariant>(Key.key("minecraft:big"))
    val CLASSIC = RegistryKey<WolfSoundVariant>(Key.key("minecraft:classic"))
    val CUTE = RegistryKey<WolfSoundVariant>(Key.key("minecraft:cute"))
    val GRUMPY = RegistryKey<WolfSoundVariant>(Key.key("minecraft:grumpy"))
    val PUGLIN = RegistryKey<WolfSoundVariant>(Key.key("minecraft:puglin"))
    val SAD = RegistryKey<WolfSoundVariant>(Key.key("minecraft:sad"))
}
