package ru.cherryngine.lib.world

import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkHeightmapType
import java.util.*

class ChunkHeightmaps(
    val heightmaps: EnumMap<ChunkHeightmapType, ChunkHeightmap> = EnumMap(ChunkHeightmapType::class.java),
) {
    fun getOrCreateHeightmap(dimensionType: DimensionType, type: ChunkHeightmapType): ChunkHeightmap =
        heightmaps.computeIfAbsent(type) { ChunkHeightmap(dimensionType, type) }

    val rawDataMap get() = heightmaps.mapValues { (_, value) -> value.getRawData() }
}