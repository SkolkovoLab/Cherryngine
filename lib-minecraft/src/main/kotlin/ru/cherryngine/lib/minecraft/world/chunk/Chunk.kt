package ru.cherryngine.lib.minecraft.world.chunk

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.block.BlockEntity
import ru.cherryngine.lib.minecraft.world.light.LightData

class Chunk(
    val sections: List<ChunkSection>,
    val blockEntities: Map<Vec3I, BlockEntity>,
    val lightData: LightData,
    val dimensionType: DimensionType,
) {
    companion object {
        fun empty(dimensionType: DimensionType) = Chunk(
            List(dimensionType.height / 16) { ChunkSection.empty() },
            emptyMap(),
            LightData(),
            dimensionType
        )
    }

    val minSection = dimensionType.minY / 16
    val maxSection = dimensionType.height / 16

    val heightmaps = ChunkHeightmaps()

    val chunkData = ChunkData(heightmaps, sections, blockEntities)

    init {
        ChunkHeightmap.Type.entries.forEach { type ->
            getOrCreateHeightmap(type)
            ChunkHeightmap.generate(this, setOf(type))
        }
    }

    fun getSection(section: Int): ChunkSection {
        return sections[section - minSection]
    }

    fun getSectionAt(blockY: Int): ChunkSection {
        return getSection(ChunkUtils.getChunkCoordinate(blockY))
    }

    fun getBlockId(blockPos: Vec3I): Int {
        val section = getSectionAt(blockPos.y)
        return section.getBlock(
            ChunkUtils.globalToSectionRelative(blockPos.x),
            ChunkUtils.globalToSectionRelative(blockPos.y),
            ChunkUtils.globalToSectionRelative(blockPos.z)
        )
    }

    fun getBlock(blockPos: Vec3I): Block {
        return Block.getBlockByStateId(getBlockId(blockPos))
    }

    fun setBlockId(blockPos: Vec3I, stateId: Int) {
        val section = getSectionAt(blockPos.y)
        return section.setBlock(
            ChunkUtils.globalToSectionRelative(blockPos.x),
            ChunkUtils.globalToSectionRelative(blockPos.y),
            ChunkUtils.globalToSectionRelative(blockPos.z),
            stateId
        )
    }

    fun setBlock(blockPos: Vec3I, block: Block) {
        setBlockId(blockPos, block.getStateId())
    }

    fun getOrCreateHeightmap(type: ChunkHeightmap.Type): ChunkHeightmap = heightmaps.getOrCreateHeightmap(this, type)
}