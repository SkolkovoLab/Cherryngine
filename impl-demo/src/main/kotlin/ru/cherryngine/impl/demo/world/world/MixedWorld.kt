package ru.cherryngine.impl.demo.world.world

import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.world.Chunk
import ru.cherryngine.lib.minecraft.registry.Blocks
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection

class MixedWorld(
    override val name: String,
    override val dimensionType: DimensionType,
    val worlds: List<World>,
    val entitiesWorld: WorldImpl
) : World {
    init {
        if (worlds.isEmpty()) throw IllegalArgumentException("worlds is empty")
    }

    override val chunks: Map<Long, Chunk>
        get() = mixWorld(worlds.first(), worlds.drop(1))
    override val entities: Set<McEntity>
        get() = worlds.flatMap { it.entities }.toSet()
    override val mutableEntities: MutableSet<McEntity>
        get() = entitiesWorld.entities

    companion object {
        fun mixWorld(world: World, layers: List<World>): Map<Long, Chunk> {
            return world.chunks.mapValues { (chunkPos, chunk) ->
                mixChunk(chunk, layers.mapNotNull { it.chunks[chunkPos] })
            }
        }

        fun mixChunk(chunk: Chunk, layers: List<Chunk>): Chunk {
            if (layers.isEmpty()) return chunk

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
    }
}