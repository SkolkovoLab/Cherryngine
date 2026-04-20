package ru.cherryngine.engine.minecraft

import jakarta.inject.Singleton
import net.minestom.server.network.packet.server.play.data.LightData
import net.minestom.server.world.DimensionType
import ru.cherryngine.lib.minecraft.world.ChunkPos
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.world.ImmutableLayerKey
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.LayeredWorld
import java.util.concurrent.ConcurrentHashMap

private data class ChunkPoolKey(
    val layerKey: ImmutableLayerKey,
    val chunkPos: Long,
)

/**
 * Кэш скомпонованных ChunkData для наборов immutable слоёв.
 * Immutable слои не меняются, поэтому кэш не инвалидируется (кроме bake).
 */
@Singleton
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
