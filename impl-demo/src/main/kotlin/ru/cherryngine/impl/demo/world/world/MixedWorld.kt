package ru.cherryngine.impl.demo.world.world

import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.world.Chunk
import ru.cherryngine.impl.demo.world.ChunkViewable
import ru.cherryngine.impl.demo.world.EmptyChunkViewable
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.Blocks
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection

class MixedWorld(
    override val name: String,
    override val dimensionType: DimensionType,
    worlds: List<World>,
) : World {

    init {
        if (worlds.isEmpty()) throw IllegalArgumentException("worlds is empty")
    }

    override val chunks: Map<ChunkPos, Chunk> = mixWorld(worlds.first(), worlds.drop(1))
    override val chunkViewables: Map<ChunkPos, ChunkViewable> = chunks.mapValues { ChunkViewable(it.key, it.value) }

    override fun getStaticViewables(chunkPos: ChunkPos): Set<StaticViewable> {
        val chunkViewable = chunkViewables[chunkPos] ?: EmptyChunkViewable(chunkPos, dimensionType)
        return setOf(chunkViewable)
    }

    companion object {
        fun mixWorld(world: World, layers: List<World>): Map<ChunkPos, Chunk> {
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