package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.TrimPattern

object TrimPatternRegistry : DataDrivenRegistry<TrimPattern>(
    "minecraft:trim_pattern",
    "trim_pattern.json",
    TrimPattern.serializer()
)
