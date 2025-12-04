package ru.cherryngine.engine.core.world.world

import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.engine.core.world.LayerChunkViewable
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.world.World

class LayerWorldViewableProviderImpl(
    override val world: World,
) : WorldViewableProvider {
    override val chunkViewables = world.chunks.mapValues { LayerChunkViewable(it.key, it.value) }

    override fun getStaticViewables(chunkPos: ChunkPos): Set<BlocksViewable> {
        val chunkViewable = chunkViewables[chunkPos] ?: return emptySet()
        return setOf(chunkViewable)
    }
}