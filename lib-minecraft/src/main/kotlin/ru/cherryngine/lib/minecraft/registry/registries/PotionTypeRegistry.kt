package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PotionType

object PotionTypeRegistry : DataDrivenRegistry<PotionType>(
    "minecraft:potion",
    "registry/potion_type_registry.json.gz",
    PotionType.serializer()
)
