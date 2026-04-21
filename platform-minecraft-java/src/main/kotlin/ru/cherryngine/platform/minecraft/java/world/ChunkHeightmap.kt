package ru.cherryngine.platform.minecraft.java.world

import net.minestom.server.instance.heightmap.Heightmap
import net.minestom.server.world.DimensionType
import ru.cherryngine.platform.minecraft.java.utils.ChunkUtils.ceilLog2
import ru.cherryngine.platform.minecraft.java.world.utils.SimpleBitStorage

class ChunkHeightmap(
    val dimensionType: DimensionType,
    val type: Heightmap.Type,
) {
    private val bitStorage = SimpleBitStorage(ceilLog2(dimensionType.height() + 1), 256)
    fun getRawData(): LongArray = bitStorage.data

    @Suppress("AddOperatorModifier")
    fun set(x: Int, z: Int, y: Int) {
        bitStorage[indexOf(x, z)] = y - dimensionType.minY()
    }

    private fun indexOf(x: Int, z: Int): Int = x + z * 16
}
