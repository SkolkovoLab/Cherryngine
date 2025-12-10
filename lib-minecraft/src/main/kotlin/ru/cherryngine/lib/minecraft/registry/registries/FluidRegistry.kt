package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Fluid

object FluidRegistry : DataDrivenRegistry<Fluid>(
    "minecraft:fluid",
    "fluid.json",
    Fluid.serializer()
)

