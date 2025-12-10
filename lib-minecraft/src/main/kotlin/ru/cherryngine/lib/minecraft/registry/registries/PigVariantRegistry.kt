package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PigVariant

object PigVariantRegistry : DataDrivenRegistry<PigVariant>(
    "minecraft:pig_variant",
    "pig_variant.json",
    PigVariant.serializer()
)
