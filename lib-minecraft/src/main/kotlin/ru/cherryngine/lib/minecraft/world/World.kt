package ru.cherryngine.lib.minecraft.world

import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.entries.DimensionType
import ru.cherryngine.lib.minecraft.world.chunk.Chunk

class World(
    val dimensionType: DimensionType,
    val chunks: Map<ChunkPos, Chunk>,
) {
    fun getChunk(x: Int, z: Int): Chunk? {
        return chunks[ChunkPos(x, z)]
    }
}