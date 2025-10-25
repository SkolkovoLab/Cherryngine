package io.github.dockyardmc.protocol.types

import io.github.dockyardmc.tide.stream.StreamCodec

data class ChunkPos(
    val x: Int,
    val z: Int,
) {
    fun pack(): Long = pack(x, z)

    companion object {
        val STREAM_CODEC_VAR_INT = StreamCodec.of(
            StreamCodec.VAR_INT, ChunkPos::x,
            StreamCodec.VAR_INT, ChunkPos::z,
            ::ChunkPos
        )

        val STREAM_CODEC_INT = StreamCodec.of(
            StreamCodec.INT, ChunkPos::x,
            StreamCodec.INT, ChunkPos::z,
            ::ChunkPos
        )

        val STREAM_CODEC_INT_SWAPPED = StreamCodec.of(
            StreamCodec.INT, ChunkPos::z,
            StreamCodec.INT, ChunkPos::x
        ) { z, x -> ChunkPos(x, z) }

        val ZERO = ChunkPos(0, 0)

        fun pack(x: Int, z: Int): Long = x.toLong() and 0xFFFFFFFFL or (z.toLong() and 0xFFFFFFFFL shl 32)

        fun unpackX(packed: Long): Int = (packed and 0xFFFFFFFFL).toInt()

        fun unpackZ(packed: Long): Int = (packed ushr 32 and 0xFFFFFFFFL).toInt()

        fun unpack(packed: Long) = ChunkPos(unpackX(packed), unpackZ(packed))
    }
}