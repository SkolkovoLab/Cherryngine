package ru.cherryngine.lib.minecraft.utils

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.world.ChunkPos
import kotlin.math.floor
import kotlin.math.sqrt

object ChunkUtils {
    fun getChunkCoordinate(xz: Int): Int = xz shr 4
    fun getChunkCoordinate(xz: Double): Int = getChunkCoordinate(floor(xz).toInt())

    fun globalToSectionRelative(xyz: Int): Int = xyz and 0xF

    fun chunkPosFromVec3D(vec: Vec3D) = ChunkPos(getChunkCoordinate(vec.x), getChunkCoordinate(vec.z))
    fun chunkPosFromVec3I(vec: Vec3I) = ChunkPos(getChunkCoordinate(vec.x), getChunkCoordinate(vec.z))

    fun getChunksInRange(pos: ChunkPos, range: Int): List<ChunkPos> {
        val chunksInRange = (range * 2 + 1) * (range * 2 + 1)
        return List(chunksInRange) { i ->
            chunkInSpiral(i, pos.x, pos.z)
        }
    }

    private fun chunkInSpiral(id: Int, xOffset: Int, zOffset: Int): ChunkPos {
        if (id == 0) return ChunkPos(xOffset, zOffset)

        val index: Int = id - 1
        val radius: Int = floor((sqrt(index + 1.0) - 1) / 2).toInt() + 1
        val p = 8 * radius * (radius - 1) / 2
        val en = radius * 2
        val a = (1 + index - p) % (radius * 8)

        // 0 = top, 1 = right, 2 = bottom, 3 = left
        return when (a / (radius * 2)) {
            0 -> ChunkPos(a - radius + xOffset, -radius + zOffset)
            1 -> ChunkPos(radius + xOffset, a % en - radius + zOffset)
            2 -> ChunkPos(radius - a % en + xOffset, radius + zOffset)
            3 -> ChunkPos(-radius + xOffset, radius - a % en + zOffset)
            else -> ChunkPos.ZERO
        }
    }

    private val MULTIPLY_DE_BRUIJN_BIT_POSITION = intArrayOf(
        0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8,
        31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
    )

    fun ceilLog2(value: Int): Int {
        val temp = if (value != 0 && (value and (value - 1)) == 0) value else roundUpPow2(value)
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(temp.toLong() * 125613361L shr 27 and 31).toInt()]
    }

    private fun roundUpPow2(value: Int): Int {
        var temp = value - 1
        temp = temp or (temp shr 1)
        temp = temp or (temp shr 2)
        temp = temp or (temp shr 4)
        temp = temp or (temp shr 8)
        temp = temp or (temp shr 16)
        return temp + 1
    }
}
