package io.github.dockyardmc.world.test

import io.github.dockyardmc.world.Light
import io.github.dockyardmc.world.chunk.ChunkData

class Chunk(
    val chunkData: ChunkData,
    val light: Light
) {
    companion object {
        val EMPTY = Chunk(ChunkData(mapOf(), listOf(), listOf()), Light())
    }
}