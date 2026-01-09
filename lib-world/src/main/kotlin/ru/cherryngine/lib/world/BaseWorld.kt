package ru.cherryngine.lib.world

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.light.LightData

class BaseWorld(
    override val dimensionType: DimensionType
) : World {
    val sectionsMap: MutableMap<Long, ChunkSection> =
        Long2ObjectOpenHashMap() // Ключом данной мапы является SectionPosition.pack()
    val lightDataMap: MutableMap<Long, LightData> =
        Long2ObjectOpenHashMap() // Ключом данной мапы является ChunkPos.pack()
    val chunkHeightmapsMap: MutableMap<Long, ChunkHeightmaps> =
        Long2ObjectOpenHashMap() // Ключом данной мапы является ChunkPos.pack()

    override fun getSectionOrNull(position: SectionPos): ChunkSection? {
        return sectionsMap[position.pack()]
    }

    fun getSectionOrCreate(position: SectionPos): ChunkSection {
        return sectionsMap.computeIfAbsent(position.pack()) { ChunkSection.empty() }
    }

    override fun getBlock(position: Vec3I): Block? {
        val sectionPos = SectionPos.fromBlockPos(position)
        val section = getSectionOrNull(sectionPos) ?: return null
        val blockId = section.getBlock(
            ChunkUtils.globalToSectionRelative(position.x),
            ChunkUtils.globalToSectionRelative(position.y),
            ChunkUtils.globalToSectionRelative(position.z)
        )
        return Block.getBlockByStateId(blockId)
    }

    fun setBlock(
        position: Vec3I,
        block: Block
    ) {
        val sectionPos = SectionPos.fromBlockPos(position)
        val section = getSectionOrCreate(sectionPos)
        section.setBlock(
            ChunkUtils.globalToSectionRelative(position.x),
            ChunkUtils.globalToSectionRelative(position.y),
            ChunkUtils.globalToSectionRelative(position.z),
            block.getStateId()
        )
    }

    val minSection = dimensionType.minY / 16

    override fun getChunkData(pos: ChunkPos): ChunkData {
        val sections = List(dimensionType.height / 16) {
            val sectionPos = SectionPos(pos.x, it + minSection, pos.z)
            getSectionOrNull(sectionPos) ?: ChunkSection.EMPTY
        }

        return ChunkData(
            chunkHeightmapsMap[pos.pack()]?.rawDataMap ?: mapOf(),
            sections,
            mapOf() // TODO
        )
    }

    override fun getLightData(pos: ChunkPos): LightData? {
        return lightDataMap[pos.pack()]
    }
}