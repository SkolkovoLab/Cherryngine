package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.DialogActionType

object DialogActionTypes {
    // STATIC ACTIONS
    val OPEN_URL = RegistryKey<DialogActionType>(Key.key("open_url"))
    val RUN_COMMAND = RegistryKey<DialogActionType>(Key.key("run_command"))
    val SUGGEST_COMMAND = RegistryKey<DialogActionType>(Key.key("suggest_command"))
    val CHANGE_PAGE = RegistryKey<DialogActionType>(Key.key("change_page"))
    val COPY_TO_CLIPBOARD = RegistryKey<DialogActionType>(Key.key("copy_to_clipboard"))
    val SHOW_DIALOG = RegistryKey<DialogActionType>(Key.key("show_dialog"))
    val CUSTOM = RegistryKey<DialogActionType>(Key.key("custom"))

    // DYNAMIC ACTIONS
    val DYNAMIC_RUN_COMMAND = RegistryKey<DialogActionType>(Key.key("dynamic/run_command"))
    val DYNAMIC_CUSTOM = RegistryKey<DialogActionType>(Key.key("dynamic/custom"))
}