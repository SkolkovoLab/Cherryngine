package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.ChickenVariant

object ChickenVariantRegistry : KtJsonDataDrivenRegistry<ChickenVariant>(
    "minecraft:chicken_variant",
    "chicken_variant.json",
    ChickenVariant.serializer()
)

