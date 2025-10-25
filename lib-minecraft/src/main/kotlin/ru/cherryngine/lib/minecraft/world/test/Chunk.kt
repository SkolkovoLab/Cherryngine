package ru.cherryngine.lib.minecraft.world.test

import ru.cherryngine.lib.minecraft.world.Light
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData

class Chunk(
    val chunkData: ChunkData,
    val light: Light
) {
    companion object {
        val EMPTY = Chunk(ChunkData(mapOf(), listOf(), listOf()), Light())
    }
}