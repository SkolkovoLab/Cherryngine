package ru.cherryngine.engine.core.world.world

import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.engine.core.world.ChunkViewable
import ru.cherryngine.engine.core.world.EmptyChunkViewable
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.world.World

class WorldViewableProviderImpl(
    override val world: World,
) : WorldViewableProvider {
    override val chunkViewables = world.chunks.mapValues { ChunkViewable(it.key, it.value) }

    override fun getStaticViewables(chunkPos: ChunkPos): Set<BlocksViewable> {
        val chunkViewable = chunkViewables[chunkPos] ?: EmptyChunkViewable(chunkPos, world.dimensionType)
        return setOf(chunkViewable)
    }
}