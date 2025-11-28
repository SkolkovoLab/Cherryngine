package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.FrogVariant

object FrogVariantRegistry : DataDrivenRegistry<FrogVariant>(
    "minecraft:frog_variant",
    "registry/frog_variant.json.gz",
    FrogVariant.serializer()
)
