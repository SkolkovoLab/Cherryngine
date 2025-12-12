package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.FrogVariant

object FrogVariantRegistry : KtJsonDataDrivenRegistry<FrogVariant>(
    "minecraft:frog_variant",
    "frog_variant.json",
    FrogVariant.serializer()
)
