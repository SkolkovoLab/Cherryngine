package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.CatVariant

object CatVariantRegistry : DataDrivenRegistry<CatVariant>(
    "minecraft:cat_variant",
    "registry/cat_variant.json.gz",
    CatVariant.serializer()
)
