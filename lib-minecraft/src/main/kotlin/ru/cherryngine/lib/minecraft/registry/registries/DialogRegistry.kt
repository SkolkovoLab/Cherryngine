package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.entries.DialogEntry

object DialogRegistry : DynamicRegistry<DialogEntry>(
    "minecraft:dialog"
)

