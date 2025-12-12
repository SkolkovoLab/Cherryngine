package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.CowVariant

object CowVariantRegistry : KtJsonDataDrivenRegistry<CowVariant>(
    "minecraft:cow_variant",
    "cow_variant.json",
    CowVariant.serializer()
)

