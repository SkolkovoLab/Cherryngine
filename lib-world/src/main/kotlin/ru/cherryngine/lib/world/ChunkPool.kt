package ru.cherryngine.lib.world

import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.light.LightData
import java.util.concurrent.ConcurrentHashMap

/**
 * Ключ, идентифицирующий набор immutable слоёв.
 * Sorted list of (layerId, priority) для корректного equals/hashCode.
 */
data class ImmutableLayerKey(
    val entries: List<Pair<String, Int>>,
) {
    companion object {
        fun from(layers: List<LayerEntry>): ImmutableLayerKey {
            val sorted = layers.map { it.layer.id to it.priority }
                .sortedWith(compareByDescending<Pair<String, Int>> { it.second }.thenBy { it.first })
            return ImmutableLayerKey(sorted)
        }
    }
}

private data class ChunkPoolKey(
    val layerKey: ImmutableLayerKey,
    val chunkPos: Long,
)

/**
 * Кэш скомпонованных ChunkData для наборов immutable слоёв.
 * Immutable слои не меняются, поэтому кэш не инвалидируется (кроме bake).
 */
class ChunkPool {
    private val cache = ConcurrentHashMap<ChunkPoolKey, ChunkData>()

    fun get(
        key: ImmutableLayerKey,
        chunkPos: ChunkPos,
        dimensionType: DimensionType,
        immutableLayers: List<LayerEntry>,
    ): ChunkData {
        val poolKey = ChunkPoolKey(key, chunkPos.pack())
        return cache.computeIfAbsent(poolKey) {
            LayeredWorld(dimensionType, immutableLayers).getChunkData(chunkPos)
        }
    }

    fun getLightData(
        immutableLayers: List<LayerEntry>,
        chunkPos: ChunkPos,
    ): LightData? {
        return immutableLayers.firstNotNullOfOrNull { it.layer.getLightData(chunkPos) }
    }

    fun invalidate(key: ImmutableLayerKey) {
        cache.keys.removeIf { it.layerKey == key }
    }

    fun clear() {
        cache.clear()
    }
}
