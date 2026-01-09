package ru.cherryngine.lib.world

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.keys.Blocks
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.block.BlockEntity
import ru.cherryngine.lib.minecraft.world.chunk.ChunkHeightmapType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.light.LightData

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

    override fun getSectionOrNull(position: SectionPos): ChunkSection? {
        val section = world.getSectionOrNull(position)?.clone() ?: return null
        val barrierId = Registries.block[Blocks.BARRIER].value.defaultStateId
        val redGlassId = Registries.block[Blocks.RED_STAINED_GLASS].value.defaultStateId
        section.blockPalette.replace(barrierId, redGlassId)
        return section
    }

    override fun getHeightMaps(pos: ChunkPos): Map<ChunkHeightmapType, LongArray> {
        return world.getHeightMaps(pos)
    }

    override fun getBlockEntities(pos: ChunkPos): Map<Vec3I, BlockEntity> {
        return world.getBlockEntities(pos)
    }
}