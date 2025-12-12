package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PotionType

object PotionTypeRegistry : KtJsonDataDrivenRegistry<PotionType>(
    "minecraft:potion_type",
    "potion_type.json",
    PotionType.serializer()
)
