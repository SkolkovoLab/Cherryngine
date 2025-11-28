package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Biome

object BiomeRegistry : DataDrivenRegistry<Biome>(
    "minecraft:worldgen/biome",
    "registry/biome_registry.json.gz",
    Biome.serializer()
)
