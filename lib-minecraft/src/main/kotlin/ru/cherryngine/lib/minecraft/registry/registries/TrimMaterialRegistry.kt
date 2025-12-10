package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.TrimMaterial

object TrimMaterialRegistry : DataDrivenRegistry<TrimMaterial>(
    "minecraft:trim_material",
    "trim_material.json",
    TrimMaterial.serializer()
)
