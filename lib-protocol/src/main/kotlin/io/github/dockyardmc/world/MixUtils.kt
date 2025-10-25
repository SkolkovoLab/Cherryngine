package io.github.dockyardmc.world

import io.github.dockyardmc.registry.Blocks
import io.github.dockyardmc.world.chunk.ChunkData
import io.github.dockyardmc.world.chunk.ChunkSection
import io.github.dockyardmc.world.test.Chunk
import io.github.dockyardmc.world.test.World
import kotlin.collections.forEach

fun mixWorld(world: World, layers: List<World>): World {
    val newChunks: Map<Long, Chunk> = world.chunks.mapValues { (chunkPos, chunk) ->
        mixChunk(chunk, layers.mapNotNull { it.chunks[chunkPos] })
    }
    return World(world.name, world.dimensionType, newChunks)
}

fun mixChunk(chunk: Chunk, layers: List<Chunk>): Chunk {
    val newSections = chunk.chunkData.sections.mapIndexed { index, section ->
        mixSection(section, layers.map { it.chunkData.sections[index] })
    }

    return Chunk(ChunkData(chunk.chunkData.heightmaps, newSections, chunk.chunkData.blockEntities), chunk.light)
}

fun mixSection(section: ChunkSection, layers: List<ChunkSection>): ChunkSection {
    val structVoidId = Blocks.STRUCTURE_VOID.defaultBlockStateId
    val newSection = ChunkSection.empty()

    for (x in 0..<16) for (y in 0..<16) for (z in 0..<16) {
        newSection.setBlock(x, y, z, section.getBlock(x, y, z))
        layers.forEach { sectionLayer ->
            val block = sectionLayer.getBlock(x, y, z)
            if (block == structVoidId) {
                newSection.setBlock(x, y, z, 0)
            } else if (block != 0) {
                newSection.setBlock(x, y, z, block)
            }
        }
    }
    return newSection
}

