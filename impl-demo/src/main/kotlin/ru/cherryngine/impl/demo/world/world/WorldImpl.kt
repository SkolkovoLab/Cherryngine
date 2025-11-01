package ru.cherryngine.impl.demo.world.world

import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.view.Viewable
import ru.cherryngine.impl.demo.world.Chunk
import ru.cherryngine.impl.demo.world.ChunkViewable
import ru.cherryngine.impl.demo.world.EmptyChunkViewable
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType

class WorldImpl(
    override val name: String,
    override val dimensionType: DimensionType,
    override val chunks: Map<ChunkPos, Chunk>,
    override val entities: MutableSet<McEntity> = mutableSetOf(),
) : World {
    override val mutableEntities: MutableSet<McEntity> get() = entities

    override val chunkViewables = chunks.mapValues { ChunkViewable(it.key, it.value) }
    override val viewables: Set<Viewable> get() = entities

    override fun getStaticViewables(chunkPos: ChunkPos): Set<StaticViewable> {
        val chunkViewable = chunkViewables[chunkPos] ?: EmptyChunkViewable(chunkPos)
        return setOf(chunkViewable)
    }
}