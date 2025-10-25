package io.github.dockyardmc.utils

import io.github.dockyardmc.cherry.math.Vec3D
import io.github.dockyardmc.cherry.math.Vec3I
import io.github.dockyardmc.protocol.types.ChunkPos
import kotlin.math.abs
import kotlin.math.floor

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
    fun chunkPosFromVec3D(vec3D: Vec3D) = ChunkPos(getChunkCoordinate(vec3D.x), getChunkCoordinate(vec3D.z))

    fun forDifferingChunksInRange(center: ChunkPos, oldCenter: ChunkPos, range: Int): List<ChunkPos> {
        val list = mutableListOf<ChunkPos>()
        for (x in center.x - range..center.x + range) {
            for (z in center.z - range..center.z + range) {
                // If the difference between either the x and old x or z and old z is > range, then the chunk is
                // newly in range, and we can process it.
                if (abs(x - oldCenter.x) > range || abs(z - oldCenter.z) > range) list.add(ChunkPos(x, z))
            }
        }
        return list
    }
}