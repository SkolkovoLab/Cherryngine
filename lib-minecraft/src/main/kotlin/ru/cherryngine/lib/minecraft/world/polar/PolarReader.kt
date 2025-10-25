package ru.cherryngine.lib.minecraft.world.polar

import com.github.luben.zstd.Zstd
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StringStreamCodec
import kotlin.math.ceil
import kotlin.math.ln

object PolarReader {
    const val MAX_BLOCK_PALETTE_SIZE: Int = 16 * 16 * 16
    const val MAX_BIOME_PALETTE_SIZE: Int = 8 * 8 * 8

    @JvmOverloads
    fun read(data: ByteArray, dataConverter: PolarDataConverter = PolarDataConverter.NOOP): PolarWorld {
        var buffer = Unpooled.copiedBuffer(data)
        buffer.writerIndex(data.size) // Set write index to end so readableBytes returns remaining bytes

        val magicNumber = buffer.readInt()
        require(magicNumber == PolarWorld.MAGIC_NUMBER) { "Invalid magic number" }

        val version = buffer.readShort()
        validateVersion(version.toInt())

        val dataVersion = if (version >= PolarWorld.VERSION_DATA_CONVERTER)
            StreamCodec.VAR_INT.read(buffer)
        else
            dataConverter.defaultDataVersion()

        val compression = PolarWorld.CompressionType.fromId(buffer.readByte().toInt())
        require(compression != null) { "Invalid compression type" }
        val compressedDataLength = StreamCodec.VAR_INT.read(buffer)

        // Replace the buffer with a "decompressed" version. This is a no-op if compression is NONE.
        buffer = decompressBuffer(buffer, compression, compressedDataLength)

        val minSection = buffer.readByte()
        val maxSection = buffer.readByte()
        require(minSection < maxSection) { "Invalid section range" }

        // User (world) data
        var userData = ByteArray(0)
        if (version > PolarWorld.VERSION_WORLD_USERDATA) {
            userData = StreamCodec.BYTE_ARRAY.read(buffer)
        }

        val chunkCount = StreamCodec.VAR_INT.read(buffer)
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
        buffer: ByteBuf,
        sectionCount: Int,
    ): PolarChunk {
        val chunkX = StreamCodec.VAR_INT.read(buffer)
        val chunkZ = StreamCodec.VAR_INT.read(buffer)

        val sections = Array(sectionCount) {
            readSection(dataConverter, version, dataVersion, buffer)
        }

        val blockEntityCount = StreamCodec.VAR_INT.read(buffer)
        val blockEntities = List(blockEntityCount) {
            readBlockEntity(dataConverter, version.toInt(), dataVersion, buffer)
        }

        val heightmaps = readHeightmapData(buffer, true)

        // Objects
        var userData = ByteArray(0)
        if (version > PolarWorld.VERSION_USERDATA_OPT_BLOCK_ENT_NBT) {
            userData = StreamCodec.BYTE_ARRAY.read(buffer)
        }

        return PolarChunk(
            chunkX, chunkZ,
            sections,
            blockEntities,
            heightmaps,
            userData
        )
    }

    private fun readSection(
        dataConverter: PolarDataConverter,
        version: Short,
        dataVersion: Int,
        buffer: ByteBuf,
    ): PolarSection {
        // If section is empty exit immediately
        if (buffer.readBoolean()) return PolarSection()


        val blockPalette: Array<String> = StringStreamCodec(MAX_BIOME_PALETTE_SIZE).list().read(buffer).toTypedArray()
        if (dataVersion < dataConverter.dataVersion()) {
            dataConverter.convertBlockPalette(blockPalette, dataVersion, dataConverter.dataVersion())
        }
        upgradeGrassInPalette(blockPalette, version.toInt())
        var blockData: IntArray? = null
        if (blockPalette.size > 1) {
            blockData = IntArray(PolarSection.BLOCK_PALETTE_SIZE)

            val rawBlockData = StreamCodec.LONG_ARRAY.read(buffer)
            val bitsPerEntry = ceil(ln(blockPalette.size.toDouble()) / ln(2.0)).toInt()
            PaletteUtil.unpack(blockData, rawBlockData, bitsPerEntry)
        }

        
        val biomePalette: Array<String> = StringStreamCodec(MAX_BIOME_PALETTE_SIZE).list().read(buffer).toTypedArray()
        var biomeData: IntArray? = null
        if (biomePalette.size > 1) {
            biomeData = IntArray(PolarSection.BIOME_PALETTE_SIZE)

            val rawBiomeData = StreamCodec.LONG_ARRAY.read(buffer)
            val bitsPerEntry = ceil(ln(biomePalette.size.toDouble()) / ln(2.0)).toInt()
            PaletteUtil.unpack(biomeData, rawBiomeData, bitsPerEntry)
        }

        var blockLightContent = PolarSection.LightContent.MISSING
        var skyLightContent = PolarSection.LightContent.MISSING
        var blockLight: ByteArray? = null
        var skyLight: ByteArray? = null
        if (version > PolarWorld.VERSION_UNIFIED_LIGHT) {
            blockLightContent = if (version >= PolarWorld.VERSION_IMPROVED_LIGHT)
                PolarSection.LightContent.VALUES[buffer.readByte().toInt()]
            else
                (if (buffer.readBoolean()) PolarSection.LightContent.PRESENT else PolarSection.LightContent.MISSING)
            if (blockLightContent == PolarSection.LightContent.PRESENT) blockLight = buffer.readLightData()
            skyLightContent = if (version >= PolarWorld.VERSION_IMPROVED_LIGHT)
                PolarSection.LightContent.VALUES[buffer.readByte().toInt()]
            else
                (if (buffer.readBoolean()) PolarSection.LightContent.PRESENT else PolarSection.LightContent.MISSING)
            if (skyLightContent == PolarSection.LightContent.PRESENT) skyLight = buffer.readLightData()
        } else if (buffer.readBoolean()) {
            blockLightContent = PolarSection.LightContent.PRESENT
            blockLight = buffer.readLightData()
            skyLightContent = PolarSection.LightContent.PRESENT
            skyLight = buffer.readLightData()
        }

        return PolarSection(
            blockPalette, blockData,
            biomePalette, biomeData,
            blockLightContent, blockLight,
            skyLightContent, skyLight
        )
    }

    fun ByteBuf.readLightData(): ByteArray {
        return ByteArray(2048).apply { readBytes(this) }
    }

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

    fun readHeightmapData(buffer: ByteBuf, skip: Boolean): Array<IntArray?>? {
        val heightmaps = if (!skip) arrayOfNulls<IntArray>(PolarChunk.MAX_HEIGHTMAPS) else null
        val heightmapMask = buffer.readInt()
        for (i in 0..<PolarChunk.MAX_HEIGHTMAPS) {
            if ((heightmapMask and (1 shl i)) == 0) continue

            if (!skip) {
                val packed = StreamCodec.LONG_ARRAY.read(buffer)
                if (packed.isEmpty()) {
                    heightmaps!![i] = IntArray(0)
                } else {
                    val bitsPerEntry = packed.size * 64 / PolarChunk.HEIGHTMAP_SIZE
                    heightmaps!![i] = IntArray(PolarChunk.HEIGHTMAP_SIZE)
                    PaletteUtil.unpack(heightmaps[i]!!, packed, bitsPerEntry)
                }
            } else {
                buffer.readBytes(StreamCodec.VAR_INT.read(buffer) * 8) // Skip a long array
            }
        }
        return heightmaps
    }

    fun readBlockEntity(
        dataConverter: PolarDataConverter,
        version: Int,
        dataVersion: Int,
        buffer: ByteBuf,
    ): PolarChunk.BlockEntity {
        val posIndex = buffer.readInt()
        var id = StreamCodec.STRING.optional().read(buffer)

        var nbt: CompoundBinaryTag? = CompoundBinaryTag.empty()
        if (version <= PolarWorld.VERSION_USERDATA_OPT_BLOCK_ENT_NBT || buffer.readBoolean()) {
            nbt = StreamCodecNBT.COMPOUND_STREAM.read(buffer)
        }

        if (dataVersion < dataConverter.dataVersion()) {
            val converted = dataConverter.convertBlockEntityData(
                if (id == null) "" else id,
                nbt!!,
                dataVersion,
                dataConverter.dataVersion()
            )
            id = converted.key
            if (id!!.isEmpty()) id = null
            nbt = converted.value
            if (nbt!!.size() == 0) nbt = null
        }

        return PolarChunk.BlockEntity(
            chunkBlockIndexGetX(posIndex),
            chunkBlockIndexGetY(posIndex),
            chunkBlockIndexGetZ(posIndex),
            id, nbt
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
        buffer: ByteBuf,
        compression: PolarWorld.CompressionType,
        length: Int,
    ): ByteBuf {
        return when (compression) {
            PolarWorld.CompressionType.NONE -> buffer
            PolarWorld.CompressionType.ZSTD -> {
                val bytes = Zstd.decompress(StreamCodec.RAW_BYTES_ARRAY.read(buffer), length)
                val newBuffer = Unpooled.copiedBuffer(bytes)
                newBuffer.writerIndex(bytes.size)
                newBuffer
            }
        }
    }

    fun chunkBlockIndexGetX(index: Int): Int {
        return index and 0xF // bits 0-3
    }

    fun chunkBlockIndexGetY(index: Int): Int {
        var y = (index and 0x07FFFFF0) ushr 4
        if ((index and 0x08000000) != 0) y = -y // Sign bit set, invert sign

        return y // 4-28 bits
    }

    fun chunkBlockIndexGetZ(index: Int): Int {
        return (index ushr 28) and 0xF // bits 28-31
    }
}
