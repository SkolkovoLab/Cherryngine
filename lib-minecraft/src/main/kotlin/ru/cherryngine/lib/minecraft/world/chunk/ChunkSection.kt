package ru.cherryngine.lib.minecraft.world.chunk

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import ru.cherryngine.lib.minecraft.registry.Biomes
import ru.cherryngine.lib.minecraft.tide.codec.CodecUtils.byteBufBytes
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.use
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.palette.Palette

class ChunkSection(
    private var blockPalette: Palette,
    private var biomePalette: Palette,
) {
    fun fillBiome(biome: Int) {
        biomePalette.fill(biome)
    }

    fun fillBlock(block: Int) {
        blockPalette.fill(block)
    }

    fun getBlock(x: Int, y: Int, z: Int): Int {
        return blockPalette[x, y, z]
    }

    fun getBiome(x: Int, y: Int, z: Int): Int {
        return biomePalette[x, y, z]
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Int) {
        blockPalette[x, y, z] = block
    }

    fun setBiome(x: Int, y: Int, z: Int, biome: Int) {
        biomePalette[x, y, z] = biome
    }

    fun hasOnlyAir(): Boolean {
        return blockPalette.isEmpty()
    }

    fun getAndSetBlock(x: Int, y: Int, z: Int, block: Int): Int {
        val id = blockPalette[x, y, z]
        blockPalette[x, y, z] = block
        return id
    }

    companion object {
        fun empty(): ChunkSection {
            val defaultBlocks = Palette.blocks()
            val defaultBiomes = Palette.biomes()
            defaultBlocks.fill(Block.AIR.getStateId())
            defaultBiomes.fill(Biomes.THE_VOID.getProtocolId())
            return ChunkSection(defaultBlocks, defaultBiomes)
        }

        val STREAM_CODEC = object : StreamCodec<ChunkSection> {
            override fun write(buffer: ByteBuf, value: ChunkSection) {
                StreamCodec.SHORT.write(buffer, value.blockPalette.count().toShort())
                Palette.BLOCK_STREAM_CODEC.write(buffer, value.blockPalette)
                Palette.BIOME_STREAM_CODEC.write(buffer, value.biomePalette)
            }

            override fun read(buffer: ByteBuf): ChunkSection {
                StreamCodec.SHORT.read(buffer) // blockPalette.count
                val blockPalette = Palette.BLOCK_STREAM_CODEC.read(buffer)
                val biomePalette = Palette.BIOME_STREAM_CODEC.read(buffer)
                return ChunkSection(blockPalette, biomePalette)
            }
        }

        val STREAM_CODEC_LIST = object : StreamCodec<List<ChunkSection>> {
            override fun write(buffer: ByteBuf, value: List<ChunkSection>) {
                val data = byteBufBytes { b ->
                    value.forEach { section ->
                        STREAM_CODEC.write(b, section)
                    }
                }
                StreamCodec.BYTE_ARRAY.write(buffer, data)
            }

            override fun read(buffer: ByteBuf): List<ChunkSection> {
                val data = StreamCodec.BYTE_ARRAY.read(buffer)
                val result = mutableListOf<ChunkSection>()
                Unpooled.wrappedBuffer(data).use { b ->
                    while (b.isReadable) {
                        result += STREAM_CODEC.read(b)
                    }
                }
                return result
            }
        }
    }
}