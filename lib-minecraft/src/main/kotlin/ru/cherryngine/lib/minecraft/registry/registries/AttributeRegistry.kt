package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Attribute

object AttributeRegistry : KtJsonDataDrivenRegistry<Attribute>(
    "minecraft:attribute",
    "attribute.json",
    Attribute.serializer()
)
