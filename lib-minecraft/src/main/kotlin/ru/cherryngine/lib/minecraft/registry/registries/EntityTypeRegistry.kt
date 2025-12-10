package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.EntityType

object EntityTypeRegistry : DataDrivenRegistry<EntityType>(
    "minecraft:entity_type",
    "entity_type.json",
    EntityType.serializer()
)
