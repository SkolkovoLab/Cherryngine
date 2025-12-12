package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.DamageType

object DamageTypeRegistry : KtJsonDataDrivenRegistry<DamageType>(
    "minecraft:damage_type",
    "damage_type.json",
    DamageType.serializer()
)

