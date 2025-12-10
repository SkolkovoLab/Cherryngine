package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PaintingVariant

object PaintingVariantRegistry : DataDrivenRegistry<PaintingVariant>(
    "minecraft:painting_variant",
    "painting_variant.json",
    PaintingVariant.serializer()
)
