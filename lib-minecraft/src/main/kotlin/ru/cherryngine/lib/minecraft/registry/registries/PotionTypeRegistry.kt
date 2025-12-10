package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PotionType

object PotionTypeRegistry : DataDrivenRegistry<PotionType>(
    "minecraft:potion_type",
    "potion_type.json",
    PotionType.serializer()
)
