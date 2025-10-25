package ru.cherryngine.lib.minecraft.registry

import ru.cherryngine.lib.minecraft.registry.registries.DialogTypeRegistry

object DialogTypes {
    val NOTICE = DialogTypeRegistry["minecraft:notice"]
    val SERVER_LINKS = DialogTypeRegistry["minecraft:server_links"]
    val DIALOG_LIST = DialogTypeRegistry["minecraft:dialog_list"]
    val MULTI_ACTION = DialogTypeRegistry["minecraft:multi_action"]
    val CONFIRMATION = DialogTypeRegistry["minecraft:confirmation"]
}
