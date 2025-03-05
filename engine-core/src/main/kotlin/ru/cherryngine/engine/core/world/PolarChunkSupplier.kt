package ru.cherryngine.engine.core.world

import net.hollowcube.polar.PolarReader
import net.hollowcube.polar.PolarSection
import net.minestom.server.command.builder.arguments.minecraft.ArgumentBlockState
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.registry.Registries
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.biome.Biome
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.cherryngine.engine.core.world.api.Chunk
import ru.cherryngine.engine.core.world.api.ChunkSupplier
import java.util.concurrent.ConcurrentHashMap

// Need to refactor
class PolarChunkSupplier(
    data: ByteArray,
    private val registries: Registries,
) : ChunkSupplier {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val worldData = PolarReader.read(data)

    private val plainsBiomeId = registries.biome().getId(Biome.PLAINS)
    private val biomeReadCache: MutableMap<String, Int> = ConcurrentHashMap()
    private val loadLighting = true
    override fun create(blockHolder: BlockHolder, chunkX: Int, chunkZ: Int): Chunk? {
        val polarChunk = worldData.chunkAt(chunkX, chunkZ)
            ?: return null // ?: return ChunkTestImpl(blockHolder.sectionCount) // FIXME фикс краша при нулл чанке
        val polarSections = polarChunk.sections
        val sections = Array(polarSections.size) {
            loadSection(polarSections[it])
        }
        return Chunk.Impl(sections)
    }

    private fun loadSection(sectionData: PolarSection): Section {
        return Section().apply { loadSection(sectionData, this) }
    }

    private fun getBiomeId(name: String): Int {
        val biomeId = registries.biome().getId(NamespaceID.from(name))
        if (biomeId != -1) return biomeId

        logger.error("Failed to find biome: $name")
        return plainsBiomeId
    }

    private fun loadSection(sectionData: PolarSection, section: Section) {
        // Blocks

        val rawBlockPalette = sectionData.blockPalette()
        val blockPalette = arrayOfNulls<Block>(rawBlockPalette.size)
        for (i in rawBlockPalette.indices) {
            try {
                blockPalette[i] = ArgumentBlockState.staticParse(
                    rawBlockPalette[i]
                )
            } catch (e: ArgumentSyntaxException) {
                logger.error("Failed to parse block state: {} ({})", rawBlockPalette[i], e.message)
                blockPalette[i] = Block.AIR
            }
        }
        if (blockPalette.size == 1) {
            section.blockPalette().fill(blockPalette[0]!!.stateId().toInt())
        } else {
            val paletteData = sectionData.blockData()
            section.blockPalette().setAll { x: Int, y: Int, z: Int ->
                val index =
                    y * net.minestom.server.instance.Chunk.CHUNK_SECTION_SIZE * net.minestom.server.instance.Chunk.CHUNK_SECTION_SIZE + z * net.minestom.server.instance.Chunk.CHUNK_SECTION_SIZE + x
                blockPalette[paletteData[index]]!!.stateId().toInt()
            }
        }

        // Biomes
        val rawBiomePalette = sectionData.biomePalette()
        val biomePalette = IntArray(rawBiomePalette.size)
        for (i in rawBiomePalette.indices) {
            biomePalette[i] = biomeReadCache.computeIfAbsent(rawBiomePalette[i]) { name: String ->
                var biomeId: Int = getBiomeId(name)
                if (biomeId == -1) {
                    logger.error("Failed to find biome: {}", name)
                    biomeId = plainsBiomeId
                }
                biomeId
            }
        }
        if (biomePalette.size == 1) {
            section.biomePalette().fill(biomePalette[0])
        } else {
            val paletteData = sectionData.biomeData()
            section.biomePalette().setAll { x: Int, y: Int, z: Int ->
                val index = x / 4 + (z / 4) * 4 + (y / 4) * 16
                val paletteIndex = paletteData[index]
                if (paletteIndex >= biomePalette.size) {
                    logger.error("Invalid biome palette index. This is probably a corrupted world, but it has been loaded with plains instead. No data has been written.")
                    return@setAll plainsBiomeId
                }
                biomePalette[paletteIndex]
            }
        }

        // Light
        if (loadLighting && sectionData.hasBlockLightData()) section.setBlockLight(sectionData.blockLight())
        if (loadLighting && sectionData.hasSkyLightData()) section.setSkyLight(sectionData.skyLight())
    }

    private fun PolarSection.hasBlockLightData(): Boolean {
        return try {
            blockLight() != null
        } catch (_: AssertionError) {
            false
        }
    }

    private fun PolarSection.hasSkyLightData(): Boolean {
        return try {
            skyLight() != null
        } catch (_: AssertionError) {
            false
        }
    }

}

