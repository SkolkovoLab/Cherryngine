package ru.cherryngine.lib.minecraft.world.chunk

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.registry.Biomes
import ru.cherryngine.lib.minecraft.registry.Blocks
import ru.cherryngine.lib.minecraft.registry.registries.BiomeRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.palette.Palette

class ChunkSection(
    private var blockPalette: Palette,
    private var biomePalette: Palette,
) {
    var nonEmptyBlockCount: Int = 0

    init {
        recountNonEmptyBlocks()
    }

    fun fillBiome(biome: Int) {
        biomePalette.fill(biome)
    }

    fun fillBlock(block: Int) {
        blockPalette.fill(block)
        recountNonEmptyBlocks()
    }

    fun getBlock(x: Int, y: Int, z: Int): Int {
        return blockPalette[x, y, z]
    }

    fun getBiome(x: Int, y: Int, z: Int): Int {
        return biomePalette[x, y, z]
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Int) {
        val old = getAndSetBlock(x, y, z, block)
        if (old != 0) nonEmptyBlockCount--
        if (block != 0) nonEmptyBlockCount++
    }

    fun setBiome(x: Int, y: Int, z: Int, biome: Int) {
        biomePalette[x, y, z] = biome
    }

    fun hasOnlyAir(): Boolean = nonEmptyBlockCount == 0

    fun getAndSetBlock(x: Int, y: Int, z: Int, block: Int): Int {
        val id = blockPalette[x, y, z]
        blockPalette[x, y, z] = block
        return id
    }

    private fun recountNonEmptyBlocks() {
        nonEmptyBlockCount = 0
        for (y in 0..<16) for (z in 0..<16) for (x in 0..<16) {
            if (blockPalette[x, y, z] != 0) nonEmptyBlockCount++
        }
    }

    companion object {
        fun empty(): ChunkSection {
            val defaultBlocks = Palette.blocks()
            val defaultBiomes = Palette.biomes()
            defaultBlocks.fill(0)
            defaultBiomes.fill(Biomes.THE_VOID.getProtocolId())
            return ChunkSection(defaultBlocks, defaultBiomes)
        }

        val STREAM_CODEC = object : StreamCodec<ChunkSection> {
            override fun write(buffer: ByteBuf, value: ChunkSection) {
                buffer.writeShort(value.blockPalette.count())
                Palette.BLOCK_SERIALIZER.write(buffer, value.blockPalette)
                Palette.biomeSerializer(BiomeRegistry.getEntries().size).write(buffer, value.biomePalette)
            }

            override fun read(buffer: ByteBuf): ChunkSection {
                TODO("Not yet implemented")
            }
        }
    }
}