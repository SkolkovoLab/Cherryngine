package ru.cherryngine.lib.minecraft.world.chunk

import ru.cherryngine.lib.minecraft.world.block.Block
import java.util.function.Predicate

object ChunkHeightmap {
    private val NOT_AIR: Predicate<Block> = Predicate { block -> !block.isAir() }
    private val BLOCKS_MOTION: Predicate<Block> = Predicate { block -> block.registryBlock.isSolid }

    enum class Type(val usage: Usage, val predicate: Predicate<Block>) {
        WORLD_SURFACE_WG(Usage.WORLD_GENERATION, NOT_AIR),
        WORLD_SURFACE(Usage.CLIENT, NOT_AIR),
        OCEAN_FLOOR_WG(Usage.WORLD_GENERATION, BLOCKS_MOTION),
        OCEAN_FLOOR(Usage.LIVE_WORLD, BLOCKS_MOTION),
        MOTION_BLOCKING(Usage.CLIENT, { block -> block.registryBlock.isSolid || block.registryBlock.isLiquid }),
        MOTION_BLOCKING_NO_LEAVES(Usage.LIVE_WORLD, { block -> block.registryBlock.isSolid && !block.identifier.endsWith("_leaves") });

        fun sendToClient(): Boolean = usage == Usage.CLIENT
    }

    enum class Usage {
        WORLD_GENERATION,
        LIVE_WORLD,
        CLIENT
    }
}