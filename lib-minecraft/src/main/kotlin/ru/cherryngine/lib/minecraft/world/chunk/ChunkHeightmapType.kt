package ru.cherryngine.lib.minecraft.world.chunk

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.world.block.Block
import java.util.function.Predicate

enum class ChunkHeightmapType(val id: Int, val predicate: Predicate<Block>) {
    WORLD_SURFACE(1, { block -> !block.isAir() }),
    MOTION_BLOCKING(4, { block -> block.registryBlock.solid || block.registryBlock.liquid }),
    MOTION_BLOCKING_NO_LEAVES(5, { block -> block.registryBlock.solid && !block.identifier.endsWith("_leaves") });

    companion object {
        val BY_ID = entries.associateBy { it.id }
        val STREAM_CODEC = StreamCodec.VAR_INT.transform<ChunkHeightmapType>({ BY_ID[it]!! }, { it.id })
    }
}