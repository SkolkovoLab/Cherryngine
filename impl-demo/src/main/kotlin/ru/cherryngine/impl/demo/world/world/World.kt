package ru.cherryngine.impl.demo.world.world

import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.world.Chunk
import ru.cherryngine.impl.demo.world.ChunkViewable
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.block.Block

interface World {
    val name: String
    val dimensionType: DimensionType
    val chunks: Map<Long, Chunk>
    val entities: Set<McEntity>
    val mutableEntities: MutableSet<McEntity>

    fun setBlock(blockPos: Vec3I, block: Block) {
        val chunkIndex = ChunkUtils.chunkPosFromVec3I(blockPos).pack()
        val chunkViewable = chunkViewables[chunkIndex]
        chunkViewable?.setBlock(blockPos, block, dimensionType)
    }

    fun setBlocks(blocks: Map<Vec3I, Block>) {
        blocks.entries.groupBy { (blockPos, _) ->
            ChunkUtils.chunkPosFromVec3I(blockPos).pack()
        }.forEach { (chunkIndex, chunkBlocks) ->
            val chunkViewable = chunkViewables[chunkIndex]
            chunkViewable?.setBlocks(chunkBlocks.associate { it.key to it.value }, dimensionType)
        }
    }

    val chunkViewables: Map<Long, ChunkViewable>
}

