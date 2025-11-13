package ru.cherryngine.lib.minecraft.utils

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sqrt

object ChunkUtils {
    fun chunkBlockIndexGetX(index: Int): Int {
        return index and 0xF // 0-4 bits
    }

    fun chunkBlockIndexGetY(index: Int): Int {
        var y = (index and 0x07FFFFF0) ushr 4
        if (((index ushr 27) and 1) == 1) y = -y // Sign bit set, invert sign

        return y // 4-28 bits
    }

    fun chunkBlockIndexGetZ(index: Int): Int {
        return (index shr 28) and 0xF // 28-32 bits
    }

    fun chunkBlockIndexGetGlobal(index: Int, chunkX: Int, chunkZ: Int): Vec3I {
        val x: Int = chunkBlockIndexGetX(index) + 16 * chunkX
        val y: Int = chunkBlockIndexGetY(index)
        val z: Int = chunkBlockIndexGetZ(index) + 16 * chunkZ
        return Vec3I(x, y, z)
    }

    fun getChunkCoordinate(xz: Int): Int = xz shr 4
    fun getChunkCoordinate(xz: Double): Int = getChunkCoordinate(floor(xz).toInt())

    fun globalToSectionRelative(xyz: Int): Int {
        return xyz and 0xF
    }

    fun chunkPosFromVec3D(vec: Vec3D) = ChunkPos(getChunkCoordinate(vec.x), getChunkCoordinate(vec.z))
    fun chunkPosFromVec3I(vec: Vec3I) = ChunkPos(getChunkCoordinate(vec.x), getChunkCoordinate(vec.z))

    fun sectionIndexFromBlockPos(blockPos: Vec3I): Long {
        val sectionPos = Vec3I(
            getChunkCoordinate(blockPos.x),
            getChunkCoordinate(blockPos.y),
            getChunkCoordinate(blockPos.z)
        )
        return sectionIndexFromSectionPos(sectionPos)
    }

    fun sectionIndexFromSectionPos(sectionPos: Vec3I): Long {
        val sectionX = sectionPos.x.toLong()
        val sectionY = sectionPos.y.toLong()
        val sectionZ = sectionPos.z.toLong()
        return ((sectionX and 0x3FFFFF) shl 42) or (sectionY and 0xFFFFF) or ((sectionZ and 0x3FFFFF) shl 20)
    }

    fun encodeBlockData(blockStateId: Int, blockLocalX: Int, blockLocalY: Int, blockLocalZ: Int): Long {
        val encodedPosition = (blockLocalX.toLong() shl 8) or (blockLocalZ.toLong() shl 4) or blockLocalY.toLong()
        return (blockStateId.toLong() shl 12) or encodedPosition
    }

    fun forDifferingChunksInRange(center: ChunkPos, oldCenter: ChunkPos, range: Int, action: (ChunkPos) -> Unit) {
        for (x in center.x - range..center.x + range) {
            for (z in center.z - range..center.z + range) {
                // If the difference between either the x and old x or z and old z is > range, then the chunk is
                // newly in range, and we can process it.
                if (abs(x - oldCenter.x) > range || abs(z - oldCenter.z) > range) action(ChunkPos(x, z))
            }
        }
    }

    fun getChunksInRange(pos: ChunkPos, range: Int): List<ChunkPos> {
        val chunksInRange = (range * 2 + 1) * (range * 2 + 1)
        return List(chunksInRange) { i ->
            chunkInSpiral(i, pos.x, pos.z)
        }
    }

    fun chunkInSpiral(id: Int, xOffset: Int = 0, zOffset: Int = 0): ChunkPos {
        // if the id is 0 then we know we're in the centre
        if (id == 0) return ChunkPos(xOffset, zOffset)

        val index: Int = id - 1

        // compute radius (inverse arithmetic sum of 8 + 16 + 24 + ...)
        val radius: Int = floor((sqrt(index + 1.0) - 1) / 2).toInt() + 1

        // compute total point on radius -1 (arithmetic sum of 8 + 16 + 24 + ...)
        val p = 8 * radius * (radius - 1) / 2

        // points by face
        val en = radius * 2

        // compute de position and shift it so the first is (-r, -r) but (-r + 1, -r)
        // so the square can connect
        val a = (1 + index - p) % (radius * 8)

        return when (a / (radius * 2)) {
            // find the face (0 = top, 1 = right, 2 = bottom, 3 = left)
            0 -> ChunkPos(a - radius + xOffset, -radius + zOffset)
            1 -> ChunkPos(radius + xOffset, a % en - radius + zOffset)
            2 -> ChunkPos(radius - a % en + xOffset, radius + zOffset)
            3 -> ChunkPos(-radius + xOffset, radius - a % en + zOffset)
            else -> ChunkPos.ZERO
        }
    }
}