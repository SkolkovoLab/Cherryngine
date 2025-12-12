package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.CatVariant

object CatVariantRegistry : KtJsonDataDrivenRegistry<CatVariant>(
    "minecraft:cat_variant",
    "cat_variant.json",
    CatVariant.serializer()
)
