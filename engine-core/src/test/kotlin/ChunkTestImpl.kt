@file:Suppress("unused")

import net.minestom.server.instance.Section
import ru.cherryngine.engine.core.world.api.Chunk

class ChunkTestImpl(
    sectionCount: Int,
) : Chunk {
    override val sections: Array<Section> = Array(sectionCount) { Section() }

    init {
        for (i in 0 until sectionCount) {
            val section = sections[i]
            val blockPalette = section.blockPalette()
            if (i < 7) {
                // blockPalette.fill(Block.STONE.stateId().toInt())

                println(blockPalette.maxSize())
            }
        }
    }
}