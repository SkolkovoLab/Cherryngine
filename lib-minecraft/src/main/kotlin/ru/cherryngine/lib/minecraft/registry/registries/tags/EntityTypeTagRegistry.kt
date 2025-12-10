package ru.cherryngine.lib.minecraft.registry.registries.tags

import ru.cherryngine.lib.minecraft.registry.TagRegistry
import ru.cherryngine.lib.minecraft.registry.registries.EntityTypeRegistry

object EntityTypeTagRegistry : TagRegistry(
    "minecraft:entity_type",
    "tags/entity_type.json",
    EntityTypeRegistry
)