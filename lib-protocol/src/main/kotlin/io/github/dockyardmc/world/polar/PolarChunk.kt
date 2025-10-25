package io.github.dockyardmc.world.polar

import net.kyori.adventure.nbt.CompoundBinaryTag

/**
 * A Java type representing the latest version of the chunk format.
 */
data class PolarChunk(
    val x: Int,
    val z: Int,
    val sections: Array<PolarSection>,
    val blockEntities: List<BlockEntity>,
    val heightmaps: Array<IntArray?>?,
    val userData: ByteArray,
) {
    fun heightmap(type: Int): IntArray {
        return heightmaps!![type]!!
    }

    data class BlockEntity(
        val x: Int,
        val y: Int,
        val z: Int,
        val id: String?,
        val data: CompoundBinaryTag?,
    )

    companion object {
        const val HEIGHTMAP_NONE: Int = 0
        const val HEIGHTMAP_MOTION_BLOCKING: Int = 1
        const val HEIGHTMAP_MOTION_BLOCKING_NO_LEAVES: Int = 2
        const val HEIGHTMAP_OCEAN_FLOOR: Int = 4
        const val HEIGHTMAP_OCEAN_FLOOR_WG: Int = 8
        const val HEIGHTMAP_WORLD_SURFACE: Int = 16
        const val HEIGHTMAP_WORLD_SURFACE_WG: Int = 32
        val HEIGHTMAPS: IntArray = intArrayOf(
            HEIGHTMAP_NONE,
            HEIGHTMAP_MOTION_BLOCKING,
            HEIGHTMAP_MOTION_BLOCKING_NO_LEAVES,
            HEIGHTMAP_OCEAN_FLOOR,
            HEIGHTMAP_OCEAN_FLOOR_WG,
            HEIGHTMAP_WORLD_SURFACE,
            HEIGHTMAP_WORLD_SURFACE_WG,
        )
        const val HEIGHTMAP_SIZE: Int = 16 * 16
        const val MAX_HEIGHTMAPS: Int = 32
    }
}
