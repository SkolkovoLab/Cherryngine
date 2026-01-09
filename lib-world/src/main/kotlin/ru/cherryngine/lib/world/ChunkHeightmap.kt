package ru.cherryngine.lib.world

import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils.ceilLog2
import ru.cherryngine.lib.minecraft.world.chunk.ChunkHeightmapType
import ru.cherryngine.lib.world.utils.SimpleBitStorage

class ChunkHeightmap(
    val dimensionType: DimensionType,
    val type: ChunkHeightmapType,
) {
    private val bitStorage = SimpleBitStorage(ceilLog2(dimensionType.height + 1), 256)
    fun getRawData(): LongArray = bitStorage.data

    @Suppress("AddOperatorModifier")
    fun set(x: Int, z: Int, y: Int) {
        bitStorage[indexOf(x, z)] = y - dimensionType.minY
    }

    private fun indexOf(x: Int, z: Int): Int = x + z * 16

}