package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.DialogBodyType

object DialogBodyTypes {
    val ITEM = RegistryKey<DialogBodyType>(Key.key("minecraft:item"))
    val PLAIN_MESSAGE = RegistryKey<DialogBodyType>(Key.key("minecraft:plain_message"))
}