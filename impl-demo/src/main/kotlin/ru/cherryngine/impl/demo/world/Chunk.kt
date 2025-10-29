package ru.cherryngine.impl.demo.world

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.utils.ChunkUtils.globalToSectionRelative
import ru.cherryngine.lib.minecraft.world.Light
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection

class Chunk(
    val chunkData: ChunkData,
    val light: Light,
) {
    companion object {
        val EMPTY = Chunk(ChunkData(mapOf(), listOf(), listOf()), Light())
    }

    fun getSection(section: Int, dimensionType: DimensionType): ChunkSection {
        val minSection = dimensionType.minY / 16
        return chunkData.sections[section - minSection]
    }

    fun getSectionAt(blockY: Int, dimensionType: DimensionType): ChunkSection {
        return getSection(ChunkUtils.getChunkCoordinate(blockY), dimensionType)
    }

    fun getBlockId(blockPos: Vec3I, dimensionType: DimensionType): Int {
        val section = getSectionAt(blockPos.y, dimensionType)
        return section.getBlock(
            globalToSectionRelative(blockPos.x),
            globalToSectionRelative(blockPos.y),
            globalToSectionRelative(blockPos.z)
        )
    }

    fun getBlock(blockPos: Vec3I, dimensionType: DimensionType): Block {
        return Block.getBlockByStateId(getBlockId(blockPos, dimensionType))
    }

    fun setBlockId(blockPos: Vec3I, stateId: Int, dimensionType: DimensionType) {
        val section = getSectionAt(blockPos.y, dimensionType)
        return section.setBlock(
            globalToSectionRelative(blockPos.x),
            globalToSectionRelative(blockPos.y),
            globalToSectionRelative(blockPos.z),
            stateId
        )
    }

    fun setBlock(blockPos: Vec3I, block: Block, dimensionType: DimensionType) {
        setBlockId(blockPos, block.getProtocolId(), dimensionType)
    }
}