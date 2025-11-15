package ru.cherryngine.engine.core.world.polar

object WorldHeightUtil {
    fun updateWorldHeight(world: PolarWorld, minSection: Byte, maxSection: Byte): PolarWorld {
        assert(minSection <= maxSection) { "minSection cannot be less than maxSection" }

        val chunks = ArrayList<PolarChunk>(world.chunks().size)

        for (chunk in world.chunks()) {
            chunks.add(updateChunkHeight(chunk, minSection, maxSection))
        }

        return PolarWorld(
            world.version(),
            world.dataVersion(),
            world.compression(),
            minSection,
            maxSection,
            world.userData(),
            chunks
        )
    }

    fun updateChunkHeight(chunk: PolarChunk, minSection: Byte, maxSection: Byte): PolarChunk {
        val sections = Array(maxSection - minSection + 1) {
            if (it < chunk.sections.size) chunk.sections[it] else PolarSection()
        }

        return PolarChunk(
            chunk.x,
            chunk.z,
            sections,
            chunk.blockEntities,
            chunk.heightmaps,
            chunk.userData
        )
    }
}
