package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.ChickenVariant

object ChickenVariantRegistry : DataDrivenRegistry<ChickenVariant>(
    "minecraft:chicken_variant",
    "chicken_variant.json",
    ChickenVariant.serializer()
)

