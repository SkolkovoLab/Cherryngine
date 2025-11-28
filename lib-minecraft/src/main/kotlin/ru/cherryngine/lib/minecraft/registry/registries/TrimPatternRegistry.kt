package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.TrimPattern

object TrimPatternRegistry : DataDrivenRegistry<TrimPattern>(
    "minecraft:trim_pattern",
    "registry/trim_pattern_registry.json.gz",
    TrimPattern.serializer()
)
