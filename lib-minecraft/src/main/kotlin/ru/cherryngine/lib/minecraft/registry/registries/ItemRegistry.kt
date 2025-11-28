package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Item

object ItemRegistry : DataDrivenRegistry<Item>(
    "minecraft:item",
    "registry/item_registry.json.gz",
    Item.serializer()
)
