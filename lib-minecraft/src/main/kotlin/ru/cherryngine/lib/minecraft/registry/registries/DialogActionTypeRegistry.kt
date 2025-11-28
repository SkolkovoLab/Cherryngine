package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.entries.DialogActionType

object DialogActionTypeRegistry : DynamicRegistry<DialogActionType>(
    "minecraft:dialog_action_type"
) {
    init {
        // this is not okay
        listOf(
            "open_url",
            "run_command",
            "suggest_command",
            "change_page",
            "copy_to_clipboard",
            "show_dialog",
            "custom",

            "dynamic/run_command",
            "dynamic/custom"
        ).forEach {
            addEntry(DialogActionType(it))
        }
        updateCache()
    }
}

