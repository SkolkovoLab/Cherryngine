package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.EntityType

object EntityTypeRegistry : KtJsonDataDrivenRegistry<EntityType>(
    "minecraft:entity_type",
    "entity_type.json",
    EntityType.serializer()
)
