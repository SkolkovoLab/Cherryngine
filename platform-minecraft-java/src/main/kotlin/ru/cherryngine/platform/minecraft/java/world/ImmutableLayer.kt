package ru.cherryngine.platform.minecraft.java.world

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.heightmap.Heightmap
import net.minestom.server.network.packet.server.play.data.LightData
import net.minestom.server.world.DimensionType
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.platform.minecraft.java.utils.ChunkUtils

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
    val sectionsMap: MutableMap<Long, Section> = Long2ObjectOpenHashMap()
    val lightDataMap: MutableMap<Long, LightData> = Long2ObjectOpenHashMap()
    val chunkHeightmapsMap: MutableMap<Long, ChunkHeightmaps> = Long2ObjectOpenHashMap()

    override fun getBlock(pos: Vec3I): Block? {
        val section = sectionsMap[SectionPos.fromBlockPos(pos).pack()] ?: return null
        val stateId = section.blockPalette().get(
            ChunkUtils.globalToSectionRelative(pos.x),
            ChunkUtils.globalToSectionRelative(pos.y),
            ChunkUtils.globalToSectionRelative(pos.z),
        )
        if (stateId == 0) return null // air = transparent, inherit from lower layer
        return Block.fromStateId(stateId) ?: Block.AIR
    }

    override fun getSectionOrNull(pos: SectionPos): Section? = sectionsMap[pos.pack()]

    override fun getLightData(pos: ChunkPos): LightData? = lightDataMap[pos.pack()]

    override fun getBlockEntities(pos: ChunkPos): Map<Vec3I, Block> = emptyMap() // TODO

    override fun getHeightMaps(pos: ChunkPos): Map<Heightmap.Type, LongArray> =
        chunkHeightmapsMap[pos.pack()]?.rawDataMap ?: emptyMap()

    fun getSectionOrCreate(pos: SectionPos): Section =
        sectionsMap.computeIfAbsent(pos.pack()) { Section() }
}
