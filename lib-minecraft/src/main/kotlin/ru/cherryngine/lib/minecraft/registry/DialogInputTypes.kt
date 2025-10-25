package ru.cherryngine.lib.minecraft.registry

import ru.cherryngine.lib.minecraft.registry.registries.DialogInputTypeRegistry

object DialogInputTypes {
    val BOOLEAN = DialogInputTypeRegistry["minecraft:boolean"]
    val NUMBER_RANGE = DialogInputTypeRegistry["minecraft:number_range"]
    val SINGLE_OPTION = DialogInputTypeRegistry["minecraft:single_option"]
    val TEXT = DialogInputTypeRegistry["minecraft:text"]
}
