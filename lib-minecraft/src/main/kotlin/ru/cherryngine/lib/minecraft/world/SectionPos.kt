package ru.cherryngine.lib.minecraft.world

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

data class SectionPos(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    fun pack(): Long = pack(x, y, z)

    companion object {
        fun pack(x: Int, y: Int, z: Int): Long {
            return (x.toLong() and 0x3FFFFF shl 42) or
                (y.toLong() and 0xFFFFF) or
                (z.toLong() and 0x3FFFFF shl 20)
        }

        fun fromBlockPos(pos: Vec3I): SectionPos = SectionPos(
            ChunkUtils.getChunkCoordinate(pos.x),
            ChunkUtils.getChunkCoordinate(pos.y),
            ChunkUtils.getChunkCoordinate(pos.z),
        )
    }
}
