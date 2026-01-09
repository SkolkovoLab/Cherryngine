package ru.cherryngine.lib.minecraft.network.protocol.types

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

data class SectionPos(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun pack(): Long = pack(x, y, z)

    companion object {
        val STREAM_CODEC: StreamCodec<SectionPos> =
            StreamCodec.LONG.transform(SectionPos::unpack, SectionPos::pack)

        fun pack(x: Int, y: Int, z: Int): Long {
            return ((x.toLong() and 0x3FFFFF) shl 42) or (y.toLong() and 0xFFFFF) or ((z.toLong() and 0x3FFFFF) shl 20)
        }

        fun unpack(value: Long): SectionPos {
            val x = (value shr 42).toInt()
            val y = (value shl 44 shr 44).toInt()
            val z = (value shl 22 shr 42).toInt()
            return SectionPos(x, y, z)
        }

        fun fromBlockPos(blockPos: Vec3I): SectionPos {
            return SectionPos(
                ChunkUtils.getChunkCoordinate(blockPos.x),
                ChunkUtils.getChunkCoordinate(blockPos.y),
                ChunkUtils.getChunkCoordinate(blockPos.z)
            )
        }
    }
}