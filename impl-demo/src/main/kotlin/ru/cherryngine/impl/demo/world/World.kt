package ru.cherryngine.impl.demo.world

import ru.cherryngine.lib.minecraft.registry.registries.DimensionType

class World(
    val name: String,
    val dimensionType: DimensionType,
    val chunks: Map<Long, Chunk>
)