package ru.cherryngine.lib.polar

import io.netty.buffer.Unpooled
import net.kyori.adventure.key.Key
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.protocol.types.BlockEntityType
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.Biomes
import ru.cherryngine.lib.minecraft.registry.registries.Biome
import ru.cherryngine.lib.minecraft.registry.registries.BiomeRegistry
import ru.cherryngine.lib.minecraft.registry.registries.BlockRegistry
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.Light
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.block.BlockEntity
import ru.cherryngine.lib.minecraft.world.chunk.Chunk
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.palette.Palette
import java.util.*

object PolarWorldGenerator {
    private val logger = LoggerFactory.getLogger(PolarWorldGenerator::class.java)

    fun loadChunks(worldBytes: ByteArray, dimensionType: DimensionType): Map<ChunkPos, Chunk> {
        val polarWorld = PolarReader.read(worldBytes)

        return polarWorld.chunks().associate { polarChunk ->
            val sectionsCount = polarChunk.sections.size
            val sections = List(sectionsCount) {
                val polarSection = polarChunk.sections[it]
                val blockPalette = Palette.blocks()
                blockPalette.setAll { x, y, z ->
                    getBlock(polarSection, x, y, z).getStateId()
                }
                val biomePalette = Palette.biomes()
                biomePalette.setAll { x, y, z ->
                    getBiome(polarSection, x, y, z).getProtocolId()
                }
                ChunkSection(blockPalette, biomePalette)
            }

            val skyLight = getLightData(polarChunk) { it.skyLight() }
            val blockLight = getLightData(polarChunk) { it.blockLight() }
            val light = Light(
                skyLight.mask,
                blockLight.mask,
                skyLight.emptyMask,
                blockLight.emptyMask,
                skyLight.light,
                blockLight.light
            )

            val blockEntities = polarChunk.blockEntities.mapNotNull { polarBlockEntity ->
                if (polarBlockEntity.id == null) return@mapNotNull null
                if (polarBlockEntity.data == null) return@mapNotNull null

                BlockEntity(
                    Vec3I(polarBlockEntity.x, polarBlockEntity.y, polarBlockEntity.z),
                    BlockEntityType.fromKey(Key.key(polarBlockEntity.id)),
                    polarBlockEntity.data
                )
            }

            val chunkData = ChunkData(
                mapOf(),
                sections,
                blockEntities
            )

            ChunkPos(polarChunk.x, polarChunk.z) to Chunk(chunkData, light, dimensionType)
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
        StreamCodec.RAW_BYTES_ARRAY.list().write(buffer, list)
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

    private fun getBiome(polarSection: PolarSection, x: Int, y: Int, z: Int): Biome {
        val biomeId = getId(polarSection.biomePalette(), polarSection.biomeData(), x, y, z)
            ?: return Biomes.PLAINS
        return BiomeRegistry[biomeId]
    }

    private fun getBlock(polarSection: PolarSection, x: Int, y: Int, z: Int): Block {
        val blockId = getId(polarSection.blockPalette(), polarSection.blockData(), x, y, z)
            ?: return Block.AIR
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
                blockId = blockId.take(bracketPos)
            }

            val registryBlock = BlockRegistry[blockId]

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
            return Block.AIR
        }
    }
}