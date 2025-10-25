package io.github.dockyardmc.world.test

import io.github.dockyardmc.registry.registries.DimensionType

class World(
    val name: String,
    val dimensionType: DimensionType,
    val chunks: Map<Long, Chunk>
)