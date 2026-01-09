package ru.cherryngine.lib.world

import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection

object MixTools {
    fun mixSection(layers: List<ChunkSection>): ChunkSection {
        val newSection = ChunkSection.empty()

        for (x in 0..<16) for (y in 0..<16) for (z in 0..<16) {
            layers.forEach { sectionLayer ->
                val block = sectionLayer.getBlock(x, y, z)
                if (block == Block.STRUCTURE_VOID.getStateId()) {
                    newSection.setBlock(x, y, z, 0)
                } else if (block != 0) {
                    newSection.setBlock(x, y, z, block)
                }
            }
        }
        return newSection
    }
}