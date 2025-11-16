package ru.cherryngine.engine.core.world.world

import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.engine.core.world.LayerChunkViewable
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.world.chunk.Chunk

class LayerWorldImpl(
    override val name: String,
    override val dimensionType: DimensionType,
    override val chunks: Map<ChunkPos, Chunk>,
) : World {
    override val chunkViewables = chunks.mapValues { LayerChunkViewable(it.key, it.value) }

    override fun getStaticViewables(chunkPos: ChunkPos): Set<BlocksViewable> {
        val chunkViewable = chunkViewables[chunkPos] ?: return emptySet()
        return setOf(chunkViewable)
    }
}