package ru.cherryngine.lib.minecraft.registry.registries.tags

import ru.cherryngine.lib.minecraft.registry.TagRegistry
import ru.cherryngine.lib.minecraft.registry.registries.ItemRegistry

object ItemTagRegistry : TagRegistry(
    "minecraft:item",
    "tags/item.json",
    ItemRegistry
)