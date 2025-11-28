package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.CowVariant

object CowVariantRegistry : DataDrivenRegistry<CowVariant>(
    "minecraft:cow_variant",
    "registry/cow_variant.json.gz",
    CowVariant.serializer()
)

