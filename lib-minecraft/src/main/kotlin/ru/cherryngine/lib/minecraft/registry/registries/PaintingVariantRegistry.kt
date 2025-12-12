package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PaintingVariant

object PaintingVariantRegistry : KtJsonDataDrivenRegistry<PaintingVariant>(
    "minecraft:painting_variant",
    "painting_variant.json",
    PaintingVariant.serializer()
)
