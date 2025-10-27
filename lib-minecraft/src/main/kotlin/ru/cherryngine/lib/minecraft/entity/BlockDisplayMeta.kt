package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.world.block.Block

@Suppress("PropertyName")
sealed class BlockDisplayMeta : DisplayMeta() {
    companion object : BlockDisplayMeta()

    val DISPLAYED_BLOCK_STATE = index(MetadataEntry.Type.BLOCK_STATE, Block.AIR)
}