package ru.cherryngine.engine.mcprotocollib

import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.light.LightData
import ru.cherryngine.lib.world.ImmutableLayerKey
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.LayeredWorld
import java.util.concurrent.ConcurrentHashMap

private data class ChunkPoolKey(
    val layerKey: ImmutableLayerKey,
    val chunkPos: Long,
)

@Singleton
class McProtocolLibChunkPool {
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
