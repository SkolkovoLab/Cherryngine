package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.CatVariant

object CatVariantRegistry : DataDrivenRegistry<CatVariant>(
    "minecraft:cat_variant",
    "cat_variant.json",
    CatVariant.serializer()
)
