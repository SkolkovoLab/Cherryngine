package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.WolfVariant

object WolfVariantRegistry : KtJsonDataDrivenRegistry<WolfVariant>(
    "minecraft:wolf_variant",
    "wolf_variant.json",
    WolfVariant.serializer()
)
