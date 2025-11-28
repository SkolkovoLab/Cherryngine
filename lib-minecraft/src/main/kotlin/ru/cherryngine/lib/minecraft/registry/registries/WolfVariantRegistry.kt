package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.WolfVariant

object WolfVariantRegistry : DataDrivenRegistry<WolfVariant>(
    "minecraft:wolf_variant",
    "registry/wolf_variant.json.gz",
    WolfVariant.serializer()
)
