package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.DialogType

object DialogTypes {
    val NOTICE = RegistryKey<DialogType>(Key.key("minecraft:notice"))
    val SERVER_LINKS = RegistryKey<DialogType>(Key.key("minecraft:server_links"))
    val DIALOG_LIST = RegistryKey<DialogType>(Key.key("minecraft:dialog_list"))
    val MULTI_ACTION = RegistryKey<DialogType>(Key.key("minecraft:multi_action"))
    val CONFIRMATION = RegistryKey<DialogType>(Key.key("minecraft:confirmation"))
}
