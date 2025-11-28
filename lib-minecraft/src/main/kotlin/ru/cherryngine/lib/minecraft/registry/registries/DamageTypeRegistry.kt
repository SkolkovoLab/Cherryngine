package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.DamageType

object DamageTypeRegistry : DataDrivenRegistry<DamageType>(
    "minecraft:damage_type",
    "registry/damage_type_registry.json.gz",
    DamageType.serializer()
)

