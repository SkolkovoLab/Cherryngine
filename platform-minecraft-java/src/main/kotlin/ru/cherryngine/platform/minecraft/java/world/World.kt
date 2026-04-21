package ru.cherryngine.platform.minecraft.java.world

import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.heightmap.Heightmap
import net.minestom.server.network.packet.server.play.data.LightData
import net.minestom.server.world.DimensionType
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.platform.minecraft.java.world.chunk.ChunkData

interface World {
    val dimensionType: DimensionType
    fun getBlock(position: Vec3I): Block?
    fun getLightData(pos: ChunkPos): LightData?
    fun getSectionOrNull(position: SectionPos): Section?

    fun getHeightMaps(pos: ChunkPos): Map<Heightmap.Type, LongArray>
    fun getBlockEntities(pos: ChunkPos): Map<Vec3I, Block>

    fun getChunkData(pos: ChunkPos): ChunkData {
        val minSection = dimensionType.minY() / 16
        val sections = List(dimensionType.height() / 16) {
            val sectionPos = SectionPos(pos.x, it + minSection, pos.z)
            getSectionOrNull(sectionPos) ?: Section()
        }

        return ChunkData(
            getHeightMaps(pos),
            sections,
            getBlockEntities(pos)
        )
    }
}
