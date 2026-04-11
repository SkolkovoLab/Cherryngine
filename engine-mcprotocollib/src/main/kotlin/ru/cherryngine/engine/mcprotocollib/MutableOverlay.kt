package ru.cherryngine.engine.mcprotocollib

import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.world.LayeredWorld

object MutableOverlay {
    /**
     * @return Map<SectionPos, List<Long>> — for each section, list of encoded block changes.
     *         Format: (blockStateId << 12) | (x << 8) | (z << 4) | y
     */
    fun computeOverlay(
        classification: LayerClassification,
        dimensionType: DimensionType,
        chunkPos: ChunkPos,
        baseChunkData: ChunkData,
    ): Map<SectionPos, List<Long>> {
        if (classification.mutableLayers.isEmpty()) return emptyMap()

        val minSection = dimensionType.minY / 16
        val sectionCount = dimensionType.height / 16
        val result = mutableMapOf<SectionPos, List<Long>>()

        val fullWorld = LayeredWorld(dimensionType, classification.allLayersSorted)

        for (sIdx in 0 until sectionCount) {
            val sectionY = sIdx + minSection
            val sectionPos = SectionPos(chunkPos.x, sectionY, chunkPos.z)

            val hasMutableSection = classification.mutableLayers.any {
                it.layer.getSectionOrNull(sectionPos) != null
            }
            if (!hasMutableSection) continue

            val baseSection = baseChunkData.sections[sIdx]
            val changes = mutableListOf<Long>()

            val baseX = chunkPos.x * 16
            val baseY = sectionY * 16
            val baseZ = chunkPos.z * 16

            for (x in 0 until 16) for (y in 0 until 16) for (z in 0 until 16) {
                val baseStateId = baseSection.getBlock(x, y, z)
                val fullBlock = fullWorld.getBlock(baseX + x, baseY + y, baseZ + z)
                val fullStateId = fullBlock.getStateId()

                if (fullStateId != baseStateId) {
                    val encoded = (fullStateId.toLong() shl 12) or
                        ((x.toLong() shl 8) or (z.toLong() shl 4) or y.toLong())
                    changes.add(encoded)
                }
            }

            if (changes.isNotEmpty()) {
                result[sectionPos] = changes
            }
        }
        return result
    }
}
