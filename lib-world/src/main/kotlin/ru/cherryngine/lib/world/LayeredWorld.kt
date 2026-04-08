package ru.cherryngine.lib.world

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.block.BlockEntity
import ru.cherryngine.lib.minecraft.world.chunk.ChunkHeightmapType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.light.LightData

/**
 * Композитный мир из слоёв (LayerEntry). Реализует World для отправки клиенту.
 *
 * Алгоритм композиции getBlock (по CLAUDE.md):
 *   для каждого слоя сверху вниз по приоритету:
 *     block = layer.getBlock(pos)
 *     если block != null:
 *       если block == voidMarker → вернуть AIR
 *       иначе → вернуть block
 *   иначе → AIR
 */
class LayeredWorld(
    override val dimensionType: DimensionType,
    layers: List<LayerEntry> = emptyList(),
) : World {
    private val _layers: MutableList<LayerEntry> = layers.toMutableList()

    val layersSorted: List<LayerEntry> get() = _layers.sortedByDescending { it.priority }

    fun addLayer(layer: Layer, priority: Int = 0) {
        _layers.add(LayerEntry(layer, priority))
    }

    override fun getBlock(pos: Vec3I): Block {
        for (entry in layersSorted) {
            val block = entry.layer.getBlock(pos) ?: continue
            return if (block == entry.layer.voidMarker) Block.AIR else block
        }
        return Block.AIR
    }

    override fun getSectionOrNull(pos: SectionPos): ChunkSection? {
        val sorted = layersSorted
        if (sorted.none { it.layer.getSectionOrNull(pos) != null }) return null

        val result = ChunkSection.empty()
        val base = Vec3I(pos.x * 16, pos.y * 16, pos.z * 16)
        for (x in 0..<16) for (y in 0..<16) for (z in 0..<16) {
            val block = getBlock(base + Vec3I(x, y, z))
            result.setBlock(x, y, z, block.getStateId())
        }
        return result
    }

    override fun getLightData(pos: ChunkPos): LightData? =
        layersSorted.firstNotNullOfOrNull { it.layer.getLightData(pos) }

    override fun getHeightMaps(pos: ChunkPos): Map<ChunkHeightmapType, LongArray> =
        layersSorted.firstNotNullOfOrNull { it.layer.getHeightMaps(pos).takeIf { it.isNotEmpty() } } ?: emptyMap()

    override fun getBlockEntities(pos: ChunkPos): Map<Vec3I, BlockEntity> {
        val result = mutableMapOf<Vec3I, BlockEntity>()
        layersSorted.forEach { result.putAll(it.layer.getBlockEntities(pos)) }
        return result
    }
}
