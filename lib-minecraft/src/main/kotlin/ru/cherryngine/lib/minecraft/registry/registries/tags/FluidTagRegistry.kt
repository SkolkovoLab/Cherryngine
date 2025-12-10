package ru.cherryngine.lib.minecraft.registry.registries.tags

import ru.cherryngine.lib.minecraft.registry.TagRegistry
import ru.cherryngine.lib.minecraft.registry.registries.FluidRegistry

object FluidTagRegistry : TagRegistry(
    "minecraft:fluid",
    "tags/fluid.json",
    FluidRegistry
)