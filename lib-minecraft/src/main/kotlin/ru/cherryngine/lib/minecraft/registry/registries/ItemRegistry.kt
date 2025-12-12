package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Item

object ItemRegistry : KtJsonDataDrivenRegistry<Item>(
    "minecraft:item",
    "item.json",
    Item.serializer()
)
