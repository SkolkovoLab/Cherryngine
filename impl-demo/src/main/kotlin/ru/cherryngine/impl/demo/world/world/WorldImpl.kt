package ru.cherryngine.impl.demo.world.world

import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.world.Chunk
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType

class WorldImpl(
    override val name: String,
    override val dimensionType: DimensionType,
    override val chunks: Map<Long, Chunk>,
    override val entities: MutableSet<McEntity> = mutableSetOf(),
) : World {
    override val mutableEntities: MutableSet<McEntity> get() = entities
}