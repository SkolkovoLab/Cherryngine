package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.TrimPattern

object TrimPatternRegistry : KtJsonDataDrivenRegistry<TrimPattern>(
    "minecraft:trim_pattern",
    "trim_pattern.json",
    TrimPattern.serializer()
)
