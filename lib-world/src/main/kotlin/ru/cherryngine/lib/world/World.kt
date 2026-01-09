package ru.cherryngine.lib.world

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.block.BlockEntity
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.chunk.ChunkHeightmapType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.light.LightData

interface World {
    val dimensionType: DimensionType
    fun getBlock(position: Vec3I): Block?
    fun getLightData(pos: ChunkPos): LightData?
    fun getSectionOrNull(position: SectionPos): ChunkSection?

    fun getHeightMaps(pos: ChunkPos): Map<ChunkHeightmapType, LongArray>
    fun getBlockEntities(pos: ChunkPos): Map<Vec3I, BlockEntity>

    fun getChunkData(pos: ChunkPos): ChunkData {
        val minSection = dimensionType.minY / 16
        val sections = List(dimensionType.height / 16) {
            val sectionPos = SectionPos(pos.x, it + minSection, pos.z)
            getSectionOrNull(sectionPos) ?: ChunkSection.EMPTY
        }

        return ChunkData(
            getHeightMaps(pos),
            sections,
            getBlockEntities(pos)
        )
    }
}
