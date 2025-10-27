package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.world.block.Block

@Suppress("PropertyName")
sealed class EndermanMeta : MobMeta() {
    companion object : EndermanMeta()

    val CARRIED_BLOCK = index(MetadataEntry.Type.OPT_BLOCK_STATE, Block.AIR)
    val IS_SCREAMING = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_STARING = index(MetadataEntry.Type.BOOLEAN, false)
}