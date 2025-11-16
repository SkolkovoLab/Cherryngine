package ru.cherryngine.lib.minecraft.world.chunk

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.Light
import ru.cherryngine.lib.minecraft.world.block.Block

class Chunk(
    val chunkData: ChunkData,
    val light: Light,
    val dimensionType: DimensionType,
) {
    companion object {
        fun empty(dimensionType: DimensionType) = Chunk(ChunkData.empty(dimensionType), Light(), dimensionType)
    }

    fun getSection(section: Int): ChunkSection {
        val minSection = dimensionType.minY / 16
        return chunkData.sections[section - minSection]
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
        setBlockId(blockPos, block.getProtocolId())
    }
}