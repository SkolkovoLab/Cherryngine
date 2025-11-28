package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.entries.DialogInputType

object DialogInputTypeRegistry : DynamicRegistry<DialogInputType>(
    "minecraft:input_control_type"
) {
    init {
        addEntry(DialogInputType("minecraft:boolean"))
        addEntry(DialogInputType("minecraft:number_range"))
        addEntry(DialogInputType("minecraft:single_option"))
        addEntry(DialogInputType("minecraft:text"))
        updateCache()
    }
}

