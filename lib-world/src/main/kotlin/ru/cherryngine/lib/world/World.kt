package ru.cherryngine.lib.world

import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType

class World(
    val dimensionType: DimensionType,
    val chunks: Map<ChunkPos, Chunk>,
) {
    fun getChunk(x: Int, z: Int): Chunk? {
        return chunks[ChunkPos(x, z)]
    }
}