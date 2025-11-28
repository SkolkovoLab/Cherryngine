package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PaintingVariant

object PaintingVariantRegistry : DataDrivenRegistry<PaintingVariant>(
    "minecraft:painting_variant",
    "registry/painting_variant_registry.json.gz",
    PaintingVariant.serializer()
)
