package ru.cherryngine.engine.core.polar

import io.github.dockyardmc.protocol.types.writeList
import io.github.dockyardmc.registry.Biomes
import io.github.dockyardmc.registry.registries.Biome
import io.github.dockyardmc.registry.registries.BiomeRegistry
import io.github.dockyardmc.registry.registries.BlockRegistry
import io.github.dockyardmc.world.World
import io.github.dockyardmc.world.block.Block
import io.github.dockyardmc.world.chunk.ChunkSection
import io.github.dockyardmc.world.generators.WorldGenerator
import io.github.dockyardmc.world.palette.Palette
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.slf4j.LoggerFactory
import java.util.BitSet
import kotlin.math.min

class PolarWorldGenerator(
    private val worldBytes: ByteArray,
) : WorldGenerator {
    private val logger = LoggerFactory.getLogger(PolarWorldGenerator::class.java)

    override fun onWorldLoad(world: World) {

        try {
            val polarWorld = PolarReader.read(worldBytes)

            for (polarChunk in polarWorld.chunks()) {
                val chunk = world.getOrGenerateChunk(polarChunk.x, polarChunk.z)
                for (i in 0 until min(polarChunk.sections.size, chunk.sections.size)) {
                    val polarSection = polarChunk.sections[i]
                    val blockPalette = Palette.blocks()
                    blockPalette.setAll { x, y, z ->
                        getBlockId(polarSection, x, y, z).getProtocolId()
                    }
                    val biomePalette = Palette.biomes()
                    biomePalette.setAll { x, y, z ->
                        getBiomeId(polarSection, x, y, z).getProtocolId()
                    }
                    chunk.sections[i] = ChunkSection(blockPalette, biomePalette)
                }
                val blockLight = getLightData(polarChunk) { it.blockLight() }
                chunk.light.blockMask = blockLight.mask
                chunk.light.emptyBlockMask = blockLight.emptyMask
                chunk.light.blockLight = blockLight.light

                val skyLight = getLightData(polarChunk) { it.skyLight() }
                chunk.light.skyMask = skyLight.mask
                chunk.light.emptySkyMask = skyLight.emptyMask
                chunk.light.skyLight = skyLight.light

                chunk.updateCache()
            }

            logger.info("Successfully loaded Polar world with ${polarWorld.chunks().size} chunks")
        } catch (e: Exception) {
            logger.error("Failed to load Polar world", e)
            throw e
        }
    }

    private fun getLightData(polarChunk: PolarChunk, action: (PolarSection) -> ByteArray?): LightData {
        val mask = BitSet()
        val emptyMask = BitSet()
        val list = arrayListOf<ByteArray>()
        polarChunk.sections.forEachIndexed { i, section ->
            val bytes = action(section)
            if (bytes != null) {
                list.add(bytes)
                mask.set(i)
            } else {
                emptyMask.set(i)
            }
        }
        val buffer = Unpooled.buffer()
        buffer.writeList(list, ByteBuf::writeBytes)
        return LightData(mask, emptyMask, list)
    }

    private class LightData(
        var mask: BitSet,
        var emptyMask: BitSet,
        var light: List<ByteArray>,
    )

    private fun getId(palette: Array<String>, data: IntArray?, x: Int, y: Int, z: Int): String? {
        if (data == null) return palette[0]

        val blockIndex = (y shl 8) or (z shl 4) or x
        val paletteIndex = data.getOrNull(blockIndex) ?: return null

        if (paletteIndex < 0 || paletteIndex >= palette.size) {
            return null
        }

        return palette[paletteIndex]
    }

    private fun getBiomeId(polarSection: PolarSection, x: Int, y: Int, z: Int): Biome {
        val biomeId = getId(polarSection.biomePalette(), polarSection.biomeData(), x, y, z)
            ?: return Biomes.PLAINS
        return BiomeRegistry[biomeId]
    }

    private fun getBlockId(polarSection: PolarSection, x: Int, y: Int, z: Int): Block {
        val blockId = getId(polarSection.blockPalette(), polarSection.blockData(), x, y, z)
            ?: return Block.Companion.AIR
        return parseBlockState(blockId)
    }

    private fun parseBlockState(blockId: String): Block {
        try {
            var blockId = blockId
            if (!blockId.startsWith("minecraft:")) blockId = "minecraft:$blockId"

            var propertiesStr = ""
            val bracketPos = blockId.indexOf('[')
            if (bracketPos != -1) {
                propertiesStr = blockId.substring(bracketPos + 1, blockId.length - 1)
                blockId = blockId.substring(0, bracketPos)
            }

            val registryBlock = BlockRegistry[blockId]
            if (registryBlock.blockEntityId == -1) {
                println(registryBlock)
                return Block.Companion.AIR
            }

            // Парсим свойства
            if (propertiesStr.isNotEmpty()) {
                val states = propertiesStr.split(",").associate {
                    val (key, value) = it.split("=")
                    key to value
                }
                return registryBlock.withBlockStates(states)
            }

            return registryBlock.toBlock()
        } catch (e: Exception) {
            logger.error("Failed to parse block state: $blockId", e)
            return Block.Companion.AIR
        }
    }

    override fun getBlock(x: Int, y: Int, z: Int): Block {
        return Block.Companion.AIR
    }

    override fun getBiome(x: Int, y: Int, z: Int): Biome {
        return Biomes.THE_VOID
    }
}