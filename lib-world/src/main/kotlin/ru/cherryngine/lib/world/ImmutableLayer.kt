package ru.cherryngine.lib.world

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.block.BlockEntity
import ru.cherryngine.lib.minecraft.world.chunk.ChunkHeightmapType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.light.LightData

/**
 * Неизменяемый слой — загружается из файла (polar и т.п.), писать нельзя.
 *
 * Семантика getBlock:
 *   - секция отсутствует → null ("не определено в этом слое")
 *   - секция присутствует → Block (в т.ч. Block.AIR для stateId=0)
 */
class ImmutableLayer(
    val dimensionType: DimensionType,
    override val id: String,
    override val voidMarker: Block? = Block.STRUCTURE_VOID,
) : Layer {
    val sectionsMap: MutableMap<Long, ChunkSection> = Long2ObjectOpenHashMap()
    val lightDataMap: MutableMap<Long, LightData> = Long2ObjectOpenHashMap()
    val chunkHeightmapsMap: MutableMap<Long, ChunkHeightmaps> = Long2ObjectOpenHashMap()

    override fun getBlock(pos: Vec3I): Block? {
        val section = sectionsMap[SectionPos.fromBlockPos(pos).pack()] ?: return null
        val stateId = section.getBlock(
            ChunkUtils.globalToSectionRelative(pos.x),
            ChunkUtils.globalToSectionRelative(pos.y),
            ChunkUtils.globalToSectionRelative(pos.z),
        )
        return Block.getBlockByStateId(stateId)
    }

    override fun getSectionOrNull(pos: SectionPos): ChunkSection? = sectionsMap[pos.pack()]

    override fun getLightData(pos: ChunkPos): LightData? = lightDataMap[pos.pack()]

    override fun getBlockEntities(pos: ChunkPos): Map<Vec3I, BlockEntity> = emptyMap() // TODO

    override fun getHeightMaps(pos: ChunkPos): Map<ChunkHeightmapType, LongArray> =
        chunkHeightmapsMap[pos.pack()]?.rawDataMap ?: emptyMap()

    fun getSectionOrCreate(pos: SectionPos): ChunkSection =
        sectionsMap.computeIfAbsent(pos.pack()) { ChunkSection.empty() }
}
