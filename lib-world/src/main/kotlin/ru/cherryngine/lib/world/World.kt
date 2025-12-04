package ru.cherryngine.lib.world

import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.entries.DimensionType

class World(
    val dimensionType: DimensionType,
    val chunks: Map<ChunkPos, Chunk>,
) {
    fun getChunk(x: Int, z: Int): Chunk? {
        return chunks[ChunkPos(x, z)]
    }
}