package ru.cherryngine.impl.demo.world.world

import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.world.Chunk
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType

interface World {
    val name: String
    val dimensionType: DimensionType
    val chunks: Map<Long, Chunk>
    val entities: Set<McEntity>
}

