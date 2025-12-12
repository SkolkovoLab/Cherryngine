package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Fluid

object FluidRegistry : KtJsonDataDrivenRegistry<Fluid>(
    "minecraft:fluid",
    "fluid.json",
    Fluid.serializer()
)

