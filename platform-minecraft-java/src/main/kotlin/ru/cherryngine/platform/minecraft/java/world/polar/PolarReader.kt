package ru.cherryngine.platform.minecraft.java.world.polar

import com.github.luben.zstd.Zstd
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.minestom.server.network.NetworkBuffer
import kotlin.math.ceil
import kotlin.math.ln

object PolarReader {
    const val MAX_BLOCK_PALETTE_SIZE: Int = 16 * 16 * 16
    const val MAX_BIOME_PALETTE_SIZE: Int = 8 * 8 * 8

    fun read(data: ByteArray, dataConverter: PolarDataConverter = PolarDataConverter.NOOP): PolarWorld {
        var buffer: NetworkBuffer = NetworkBuffer.wrap(data, 0, data.size)

        val magicNumber = buffer.read(NetworkBuffer.INT)
        require(magicNumber == PolarWorld.MAGIC_NUMBER) { "Invalid magic number" }

        val version = buffer.read(NetworkBuffer.SHORT)
        validateVersion(version.toInt())

        val dataVersion = if (version >= PolarWorld.VERSION_DATA_CONVERTER)
            buffer.read(NetworkBuffer.VAR_INT)
        else
            dataConverter.defaultDataVersion()

        val compression = PolarWorld.CompressionType.fromId(buffer.read(NetworkBuffer.BYTE).toInt())
        require(compression != null) { "Invalid compression type" }
        val compressedDataLength = buffer.read(NetworkBuffer.VAR_INT)

        buffer = decompressBuffer(buffer, compression, compressedDataLength)

        val minSection = buffer.read(NetworkBuffer.BYTE)
        val maxSection = buffer.read(NetworkBuffer.BYTE)
        require(minSection < maxSection) { "Invalid section range" }

        var userData = ByteArray(0)
        if (version > PolarWorld.VERSION_WORLD_USERDATA) {
            userData = buffer.read(NetworkBuffer.BYTE_ARRAY)
        }

        val chunkCount = buffer.read(NetworkBuffer.VAR_INT)
        val chunks = ArrayList<PolarChunk>(chunkCount)
        repeat(chunkCount) {
            chunks.add(readChunk(dataConverter, version, dataVersion, buffer, maxSection - minSection + 1))
        }

        return PolarWorld(version, dataVersion, compression, minSection, maxSection, userData, chunks)
    }

    private fun readChunk(
        dataConverter: PolarDataConverter,
        version: Short,
        dataVersion: Int,
        buffer: NetworkBuffer,
        sectionCount: Int,
    ): PolarChunk {
        val chunkX = buffer.read(NetworkBuffer.VAR_INT)
        val chunkZ = buffer.read(NetworkBuffer.VAR_INT)

        val sections = Array(sectionCount) {
            readSection(dataConverter, version, dataVersion, buffer)
        }

        val blockEntityCount = buffer.read(NetworkBuffer.VAR_INT)
        val blockEntities = List(blockEntityCount) {
            readBlockEntity(dataConverter, version.toInt(), dataVersion, buffer)
        }

        val heightmaps = readHeightmapData(buffer, true)

        var userData = ByteArray(0)
        if (version > PolarWorld.VERSION_USERDATA_OPT_BLOCK_ENT_NBT) {
            userData = buffer.read(NetworkBuffer.BYTE_ARRAY)
        }

        return PolarChunk(
            chunkX, chunkZ,
            sections,
            blockEntities,
            heightmaps,
            userData
        )
    }

    private val STRING_LIST = NetworkBuffer.STRING.list()

    private fun readSection(
        dataConverter: PolarDataConverter,
        version: Short,
        dataVersion: Int,
        buffer: NetworkBuffer,
    ): PolarSection {
        if (buffer.read(NetworkBuffer.BOOLEAN)) return PolarSection()

        val blockPalette: Array<String> = buffer.read(STRING_LIST).toTypedArray()
        if (dataVersion < dataConverter.dataVersion()) {
            dataConverter.convertBlockPalette(blockPalette, dataVersion, dataConverter.dataVersion())
        }
        upgradeGrassInPalette(blockPalette, version.toInt())
        var blockData: IntArray? = null
        if (blockPalette.size > 1) {
            blockData = IntArray(PolarSection.BLOCK_PALETTE_SIZE)
            val rawBlockData = buffer.read(NetworkBuffer.LONG_ARRAY)
            val bitsPerEntry = ceil(ln(blockPalette.size.toDouble()) / ln(2.0)).toInt()
            unpackPalette(blockData, rawBlockData, bitsPerEntry)
        }

        val biomePalette: Array<String> = buffer.read(STRING_LIST).toTypedArray()
        var biomeData: IntArray? = null
        if (biomePalette.size > 1) {
            biomeData = IntArray(PolarSection.BIOME_PALETTE_SIZE)
            val rawBiomeData = buffer.read(NetworkBuffer.LONG_ARRAY)
            val bitsPerEntry = ceil(ln(biomePalette.size.toDouble()) / ln(2.0)).toInt()
            unpackPalette(biomeData, rawBiomeData, bitsPerEntry)
        }

        var blockLightContent = PolarSection.LightContent.MISSING
        var skyLightContent = PolarSection.LightContent.MISSING
        var blockLight: ByteArray? = null
        var skyLight: ByteArray? = null
        if (version > PolarWorld.VERSION_UNIFIED_LIGHT) {
            blockLightContent = if (version >= PolarWorld.VERSION_IMPROVED_LIGHT)
                PolarSection.LightContent.VALUES[buffer.read(NetworkBuffer.BYTE).toInt()]
            else
                (if (buffer.read(NetworkBuffer.BOOLEAN)) PolarSection.LightContent.PRESENT else PolarSection.LightContent.MISSING)
            if (blockLightContent == PolarSection.LightContent.PRESENT) blockLight = readLightData(buffer)
            skyLightContent = if (version >= PolarWorld.VERSION_IMPROVED_LIGHT)
                PolarSection.LightContent.VALUES[buffer.read(NetworkBuffer.BYTE).toInt()]
            else
                (if (buffer.read(NetworkBuffer.BOOLEAN)) PolarSection.LightContent.PRESENT else PolarSection.LightContent.MISSING)
            if (skyLightContent == PolarSection.LightContent.PRESENT) skyLight = readLightData(buffer)
        } else if (buffer.read(NetworkBuffer.BOOLEAN)) {
            blockLightContent = PolarSection.LightContent.PRESENT
            blockLight = readLightData(buffer)
            skyLightContent = PolarSection.LightContent.PRESENT
            skyLight = readLightData(buffer)
        }

        return PolarSection(
            blockPalette, blockData,
            biomePalette, biomeData,
            blockLightContent, blockLight,
            skyLightContent, skyLight
        )
    }

    private fun readLightData(buffer: NetworkBuffer): ByteArray =
        buffer.read(NetworkBuffer.FixedRawBytes(2048))

    fun upgradeGrassInPalette(blockPalette: Array<String>, version: Int) {
        if (version <= PolarWorld.VERSION_SHORT_GRASS) {
            for (i in blockPalette.indices) {
                if (blockPalette[i].contains("grass")) {
                    val strippedID =
                        blockPalette[i].split("\\[".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    if (Key.key(strippedID).value() == "grass") {
                        blockPalette[i] = "short_grass"
                    }
                }
            }
        }
    }

    fun readHeightmapData(buffer: NetworkBuffer, skip: Boolean): Array<IntArray?>? {
        val heightmaps = if (!skip) arrayOfNulls<IntArray>(PolarChunk.MAX_HEIGHTMAPS) else null
        val heightmapMask = buffer.read(NetworkBuffer.INT)
        for (i in 0..<PolarChunk.MAX_HEIGHTMAPS) {
            if ((heightmapMask and (1 shl i)) == 0) continue

            if (!skip) {
                val packed = buffer.read(NetworkBuffer.LONG_ARRAY)
                if (packed.isEmpty()) {
                    heightmaps!![i] = IntArray(0)
                } else {
                    val bitsPerEntry = packed.size * 64 / PolarChunk.HEIGHTMAP_SIZE
                    heightmaps!![i] = IntArray(PolarChunk.HEIGHTMAP_SIZE)
                    unpackPalette(heightmaps[i]!!, packed, bitsPerEntry)
                }
            } else {
                val longCount = buffer.read(NetworkBuffer.VAR_INT)
                repeat(longCount) { buffer.read(NetworkBuffer.LONG) }
            }
        }
        return heightmaps
    }

    fun readBlockEntity(
        dataConverter: PolarDataConverter,
        version: Int,
        dataVersion: Int,
        buffer: NetworkBuffer,
    ): PolarChunk.BlockEntity {
        val posIndex = buffer.read(NetworkBuffer.INT)
        var id: String = buffer.read(NetworkBuffer.STRING.optional()) ?: ""

        var nbt: CompoundBinaryTag = CompoundBinaryTag.empty()
        if (version <= PolarWorld.VERSION_USERDATA_OPT_BLOCK_ENT_NBT || buffer.read(NetworkBuffer.BOOLEAN)) {
            nbt = buffer.read(NetworkBuffer.NBT_COMPOUND)
        }

        if (dataVersion < dataConverter.dataVersion()) {
            val converted = dataConverter.convertBlockEntityData(
                id,
                nbt,
                dataVersion,
                dataConverter.dataVersion()
            )
            id = converted.key
            nbt = converted.value
        }

        return PolarChunk.BlockEntity(
            chunkBlockIndexGetX(posIndex),
            chunkBlockIndexGetY(posIndex),
            chunkBlockIndexGetZ(posIndex),
            id.takeUnless { it.isEmpty() },
            nbt.takeUnless { it.isEmpty }
        )
    }

    fun validateVersion(version: Int) {
        require(version <= PolarWorld.LATEST_VERSION) {
            "Unsupported Polar version. Up to %d is supported, found %d.".format(
                PolarWorld.LATEST_VERSION, version
            )
        }
    }

    private fun decompressBuffer(
        buffer: NetworkBuffer,
        compression: PolarWorld.CompressionType,
        length: Int,
    ): NetworkBuffer {
        return when (compression) {
            PolarWorld.CompressionType.NONE -> buffer
            PolarWorld.CompressionType.ZSTD -> {
                val remaining = buffer.readableBytes().toInt()
                val compressed = buffer.read(NetworkBuffer.FixedRawBytes(remaining))
                val bytes = Zstd.decompress(compressed, length)
                NetworkBuffer.wrap(bytes, 0, bytes.size)
            }
        }
    }

    fun chunkBlockIndexGetX(index: Int): Int = index and 0xF
    fun chunkBlockIndexGetY(index: Int): Int {
        var y = (index and 0x07FFFFF0) ushr 4
        if ((index and 0x08000000) != 0) y = -y
        return y
    }
    fun chunkBlockIndexGetZ(index: Int): Int = (index ushr 28) and 0xF

    /**
     * Распаковывает long-array, где каждая entry занимает [bitsPerEntry] бит без пересечения границ long.
     */
    private fun unpackPalette(out: IntArray, packed: LongArray, bitsPerEntry: Int) {
        if (bitsPerEntry == 0) return
        val mask = (1L shl bitsPerEntry) - 1
        val entriesPerLong = 64 / bitsPerEntry
        var index = 0
        for (word in packed) {
            for (i in 0 until entriesPerLong) {
                if (index >= out.size) return
                out[index++] = ((word ushr (i * bitsPerEntry)) and mask).toInt()
            }
        }
    }
}
