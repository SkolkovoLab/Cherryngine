package ru.cherryngine.platform.minecraft.java.world

data class ChunkPos(
    val x: Int,
    val z: Int,
) {
    fun pack(): Long = pack(x, z)

    companion object {
        val ZERO = ChunkPos(0, 0)

        fun pack(x: Int, z: Int): Long = x.toLong() and 0xFFFFFFFFL or (z.toLong() and 0xFFFFFFFFL shl 32)
        fun unpackX(packed: Long): Int = (packed and 0xFFFFFFFFL).toInt()
        fun unpackZ(packed: Long): Int = (packed ushr 32 and 0xFFFFFFFFL).toInt()
        fun unpack(packed: Long) = ChunkPos(unpackX(packed), unpackZ(packed))
    }
}
