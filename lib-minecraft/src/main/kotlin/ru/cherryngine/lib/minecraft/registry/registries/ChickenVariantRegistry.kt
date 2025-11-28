package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.ChickenVariant

object ChickenVariantRegistry : DataDrivenRegistry<ChickenVariant>(
    "minecraft:chicken_variant",
    "registry/chicken_variant.json.gz",
    ChickenVariant.serializer()
)

