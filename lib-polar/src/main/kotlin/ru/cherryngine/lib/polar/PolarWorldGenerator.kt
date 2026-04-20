package ru.cherryngine.lib.polar

import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.network.packet.server.play.data.LightData
import net.minestom.server.world.DimensionType
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.world.ChunkPos
import ru.cherryngine.lib.minecraft.world.SectionPos
import ru.cherryngine.lib.world.ImmutableLayer
import ru.cherryngine.lib.world.MutableLayer
import java.util.BitSet

object PolarWorldGenerator {
    private val logger = LoggerFactory.getLogger(PolarWorldGenerator::class.java)

    fun loadAsLayer(
        worldBytes: ByteArray,
        dimensionType: DimensionType,
        id: String,
        voidMarker: Block? = Block.STRUCTURE_VOID,
        biomeIdLookup: (String) -> Int = { 0 },
    ): ImmutableLayer {
        val layer = ImmutableLayer(dimensionType, id, voidMarker)
        loadChunks(worldBytes, biomeIdLookup).forEach { (chunkPos, result) ->
            layer.lightDataMap[chunkPos.pack()] = result.lightData
            result.sections.forEachIndexed { i, section ->
                layer.sectionsMap[SectionPos(chunkPos.x, i + dimensionType.minY() / 16, chunkPos.z).pack()] = section
            }
        }
        return layer
    }

    fun loadAsMutableLayer(
        worldBytes: ByteArray,
        dimensionType: DimensionType,
        id: String,
        voidMarker: Block? = Block.STRUCTURE_VOID,
        biomeIdLookup: (String) -> Int = { 0 },
    ): MutableLayer {
        val layer = MutableLayer(id, voidMarker)
        loadChunks(worldBytes, biomeIdLookup).forEach { (chunkPos, result) ->
            result.sections.forEachIndexed { i, section ->
                val sectionPos = SectionPos(chunkPos.x, i + dimensionType.minY() / 16, chunkPos.z)
                layer.putSection(sectionPos, section)
            }
        }
        return layer
    }

    fun loadChunks(
        worldBytes: ByteArray,
        biomeIdLookup: (String) -> Int = { 0 },
    ): Map<ChunkPos, PolarChunkResult> {
        val polarWorld = PolarReader.read(worldBytes)

        return polarWorld.chunks().associate { polarChunk ->
            val sectionsCount = polarChunk.sections.size
            val sections = List(sectionsCount) {
                val polarSection = polarChunk.sections[it]

                val blockStateIds = polarSection.blockPalette().map { parseBlockState(it).stateId() }.toIntArray()
                val blockData = polarSection.blockData()
                val section = Section()
                section.blockPalette().setAll { x, y, z ->
                    if (blockData == null) {
                        if (blockStateIds.isEmpty()) 0 else blockStateIds[0]
                    } else {
                        val blockIndex = (y shl 8) or (z shl 4) or x
                        val paletteIdx = blockData[blockIndex]
                        if (paletteIdx in blockStateIds.indices) blockStateIds[paletteIdx] else 0
                    }
                }

                val biomeIds = polarSection.biomePalette().map(biomeIdLookup).toIntArray()
                val biomeData = polarSection.biomeData()
                section.biomePalette().setAll { x, y, z ->
                    if (biomeData == null) {
                        if (biomeIds.isEmpty()) 0 else biomeIds[0]
                    } else {
                        val biomeIndex = (y shl 8) or (z shl 4) or x
                        val paletteIndex = biomeData.getOrNull(biomeIndex)
                        if (paletteIndex != null && paletteIndex in biomeIds.indices) {
                            biomeIds[paletteIndex]
                        } else {
                            0
                        }
                    }
                }

                section
            }

            val skyLight = getLightData(polarChunk) { it.skyLight() }
            val blockLight = getLightData(polarChunk) { it.blockLight() }
            val lightData = LightData(
                skyLight.mask,
                blockLight.mask,
                skyLight.emptyMask,
                blockLight.emptyMask,
                skyLight.light,
                blockLight.light
            )

            val blockEntities: Map<Vec3I, Block> = polarChunk.blockEntities.mapNotNull { polarBlockEntity ->
                val id = polarBlockEntity.id ?: return@mapNotNull null
                val nbt: CompoundBinaryTag = polarBlockEntity.data ?: return@mapNotNull null
                val baseBlock = Block.fromKey(Key.key(id)) ?: return@mapNotNull null
                Vec3I(polarBlockEntity.x, polarBlockEntity.y, polarBlockEntity.z) to baseBlock.withNbt(nbt)
            }.toMap()

            ChunkPos(polarChunk.x, polarChunk.z) to PolarChunkResult(sections, blockEntities, lightData)
        }
    }

    data class PolarChunkResult(
        val sections: List<Section>,
        val blockEntities: Map<Vec3I, Block>,
        val lightData: LightData,
    )

    private fun getLightData(polarChunk: PolarChunk, action: (PolarSection) -> ByteArray?): PolarLightData {
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
        return PolarLightData(mask, emptyMask, list)
    }

    private class PolarLightData(
        var mask: BitSet,
        var emptyMask: BitSet,
        var light: List<ByteArray>,
    )

    private fun parseBlockState(blockId: String): Block {
        try {
            var blockKey = blockId
            if (!blockKey.startsWith("minecraft:")) blockKey = "minecraft:$blockKey"

            var propertiesStr = ""
            val bracketPos = blockKey.indexOf('[')
            if (bracketPos != -1) {
                propertiesStr = blockKey.substring(bracketPos + 1, blockKey.length - 1)
                blockKey = blockKey.take(bracketPos)
            }

            if (blockKey == "minecraft:chain") blockKey = "minecraft:iron_chain"

            val base = Block.fromKey(Key.key(blockKey)) ?: return Block.AIR

            if (propertiesStr.isNotEmpty()) {
                val states = propertiesStr.split(",").associate {
                    val (key, value) = it.split("=")
                    key to value
                }
                return base.withProperties(states)
            }

            return base
        } catch (e: Exception) {
            logger.error("Failed to parse block state: $blockId", e)
            return Block.AIR
        }
    }
}
