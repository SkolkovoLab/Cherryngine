package ru.cherryngine.impl.demo.world.world

import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.world.Chunk
import ru.cherryngine.impl.demo.world.LayerChunkViewable
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType

class LayerWorldImpl(
    override val name: String,
    override val dimensionType: DimensionType,
    override val chunks: Map<ChunkPos, Chunk>,
) : World {
    override val chunkViewables = chunks.mapValues { LayerChunkViewable(it.key, it.value) }

    override fun getStaticViewables(chunkPos: ChunkPos): Set<StaticViewable> {
        val chunkViewable = chunkViewables[chunkPos] ?: return emptySet()
        return setOf(chunkViewable)
    }
}