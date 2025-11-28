package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.TrimMaterial

object TrimMaterialRegistry : DataDrivenRegistry<TrimMaterial>(
    "minecraft:trim_material",
    "registry/trim_material_registry.json.gz",
    TrimMaterial.serializer()
)
