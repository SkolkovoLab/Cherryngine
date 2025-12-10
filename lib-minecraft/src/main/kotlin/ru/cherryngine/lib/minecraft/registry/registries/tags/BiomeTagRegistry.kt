package ru.cherryngine.lib.minecraft.registry.registries.tags

import ru.cherryngine.lib.minecraft.registry.TagRegistry
import ru.cherryngine.lib.minecraft.registry.registries.BiomeRegistry

object BiomeTagRegistry : TagRegistry(
    "minecraft:worldgen/biome",
    "tags/biome.json",
    BiomeRegistry
)