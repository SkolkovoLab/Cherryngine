package ru.cherryngine.engine.core.world.api

import ru.cherryngine.engine.core.world.BlockHolder

fun interface ChunkSupplier {
    fun create(blockHolder: BlockHolder, chunkX: Int, chunkZ: Int): Chunk?
}