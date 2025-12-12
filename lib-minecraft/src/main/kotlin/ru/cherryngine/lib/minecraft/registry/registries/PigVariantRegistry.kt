package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PigVariant

object PigVariantRegistry : KtJsonDataDrivenRegistry<PigVariant>(
    "minecraft:pig_variant",
    "pig_variant.json",
    PigVariant.serializer()
)
