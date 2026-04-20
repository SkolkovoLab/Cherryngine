package ru.cherryngine.lib.world

import net.minestom.server.instance.heightmap.Heightmap
import net.minestom.server.world.DimensionType
import java.util.EnumMap

class ChunkHeightmaps(
    val heightmaps: EnumMap<Heightmap.Type, ChunkHeightmap> = EnumMap(Heightmap.Type::class.java),
) {
    fun getOrCreateHeightmap(dimensionType: DimensionType, type: Heightmap.Type): ChunkHeightmap =
        heightmaps.computeIfAbsent(type) { ChunkHeightmap(dimensionType, type) }

    val rawDataMap: Map<Heightmap.Type, LongArray>
        get() = heightmaps.mapValues { (_, value) -> value.getRawData() }
}
