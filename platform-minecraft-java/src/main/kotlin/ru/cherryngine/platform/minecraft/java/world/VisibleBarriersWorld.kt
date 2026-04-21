package ru.cherryngine.platform.minecraft.java.world

import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.heightmap.Heightmap
import net.minestom.server.network.packet.server.play.data.LightData
import net.minestom.server.world.DimensionType
import ru.cherryngine.lib.math.Vec3I

class VisibleBarriersWorld(
    val world: World,
) : World {
    override val dimensionType: DimensionType get() = world.dimensionType

    override fun getBlock(position: Vec3I): Block? {
        return world.getBlock(position)
    }

    override fun getLightData(pos: ChunkPos): LightData? {
        return world.getLightData(pos)
    }

    override fun getSectionOrNull(position: SectionPos): Section? {
        val section = world.getSectionOrNull(position)?.clone() ?: return null
        val barrierId = Block.BARRIER.stateId()
        val redGlassId = Block.RED_STAINED_GLASS.stateId()
        section.blockPalette().replace(barrierId, redGlassId)
        return section
    }

    override fun getHeightMaps(pos: ChunkPos): Map<Heightmap.Type, LongArray> {
        return world.getHeightMaps(pos)
    }

    override fun getBlockEntities(pos: ChunkPos): Map<Vec3I, Block> {
        return world.getBlockEntities(pos)
    }
}
