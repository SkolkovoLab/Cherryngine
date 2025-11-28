package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.entries.DialogType
import java.util.concurrent.atomic.AtomicInteger

object DialogTypeRegistry : DynamicRegistry<DialogType>(
    "minecraft:dialog_type"
) {
    private val dialogTypes: MutableMap<String, DialogType> = mutableMapOf()
    private val _protocolIds: MutableMap<String, Int> = mutableMapOf()
    private val protocolIdCounter = AtomicInteger()

    val protocolIds get() = _protocolIds.toMap()

    init {
        addEntry(DialogType("minecraft:notice"))
        addEntry(DialogType("minecraft:server_links"))
        addEntry(DialogType("minecraft:dialog_list"))
        addEntry(DialogType("minecraft:multi_action"))
        addEntry(DialogType("minecraft:confirmation"))
        updateCache()
    }
}

