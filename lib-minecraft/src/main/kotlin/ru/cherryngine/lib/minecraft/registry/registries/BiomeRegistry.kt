package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.CodecDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Biome

object BiomeRegistry : CodecDataDrivenRegistry<Biome>(
    "minecraft:worldgen/biome",
    "worldgen/biome.json",
    Biome.CODEC
)
