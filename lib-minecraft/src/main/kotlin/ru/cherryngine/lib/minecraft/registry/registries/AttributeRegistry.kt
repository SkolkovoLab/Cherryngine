package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Attribute

object AttributeRegistry : DataDrivenRegistry<Attribute>(
    "minecraft:attribute",
    "attribute.json",
    Attribute.serializer()
)
