package ru.cherryngine.lib.polar

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos

/**
 * A Java type representing the latest version of the world format.
 */
class PolarWorld(
    // Polar metadata
    private val version: Short = LATEST_VERSION,
    private val dataVersion: Int = DATA_VERSION,
    private var compression: CompressionType = DEFAULT_COMPRESSION,
    // World metadata
    private var minSection: Byte = (-4).toByte(),
    maxSection: Byte = 19.toByte(),
    userData: ByteArray = ByteArray(0),
    chunks: MutableList<PolarChunk> = mutableListOf(),
) {
    private var maxSection: Byte
    private var userData: ByteArray

    // Chunk data
    private val chunks: Long2ObjectMap<PolarChunk> = Long2ObjectOpenHashMap<PolarChunk>()

    init {
        this.minSection = minSection
        this.maxSection = maxSection
        this.userData = userData

        for (chunk in chunks) {
            val index = ChunkPos.pack(chunk.x, chunk.z)
            this.chunks.put(index, chunk)
        }
    }

    fun version(): Short {
        return version
    }

    fun dataVersion(): Int {
        return dataVersion
    }

    fun compression(): CompressionType {
        return compression
    }

    fun setCompression(compression: CompressionType) {
        this.compression = compression
    }

    fun minSection(): Byte {
        return minSection
    }

    fun maxSection(): Byte {
        return maxSection
    }

    fun setSectionCount(minSection: Byte, maxSection: Byte) {
        for (l in chunks.keys) {
            chunks.put(l, WorldHeightUtil.updateChunkHeight(chunks.get(l)!!, minSection, maxSection))
        }

        this.minSection = minSection
        this.maxSection = maxSection
    }

    fun userData(): ByteArray {
        return userData
    }

    fun userData(userData: ByteArray) {
        this.userData = userData
    }

    fun chunkAt(x: Int, z: Int): PolarChunk? {
        return chunks.getOrDefault(ChunkPos.pack(x, z), null)
    }

    fun updateChunkAt(x: Int, z: Int, chunk: PolarChunk) {
        chunks.put(ChunkPos.pack(x, z), chunk)
    }

    fun chunks(): MutableCollection<PolarChunk> {
        return chunks.values
    }

    enum class CompressionType {
        NONE,
        ZSTD;

        companion object {
            private val VALUES: Array<CompressionType?> = entries.toTypedArray()

            fun fromId(id: Int): CompressionType? {
                if (id < 0 || id >= VALUES.size) return null
                return VALUES[id]
            }
        }
    }

    companion object {
        const val MAGIC_NUMBER: Int = 0x506F6C72 // `Polr`
        const val LATEST_VERSION: Short = 7
        const val DATA_VERSION: Int = 4189

        const val VERSION_UNIFIED_LIGHT: Short = 1
        const val VERSION_USERDATA_OPT_BLOCK_ENT_NBT: Short = 2
        const val VERSION_MINESTOM_NBT_READ_BREAK: Short = 3
        const val VERSION_WORLD_USERDATA: Short = 4
        const val VERSION_SHORT_GRASS: Short = 5 // >:(
        const val VERSION_DATA_CONVERTER: Short = 6
        const val VERSION_IMPROVED_LIGHT: Short = 7

        var DEFAULT_COMPRESSION: CompressionType = CompressionType.ZSTD
    }
}
