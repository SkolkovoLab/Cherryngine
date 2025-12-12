package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.TrimMaterial

object TrimMaterialRegistry : KtJsonDataDrivenRegistry<TrimMaterial>(
    "minecraft:trim_material",
    "trim_material.json",
    TrimMaterial.serializer()
)
