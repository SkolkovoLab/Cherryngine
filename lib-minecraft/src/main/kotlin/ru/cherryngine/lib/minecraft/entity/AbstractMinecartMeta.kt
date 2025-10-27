package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.world.block.Block

@Suppress("PropertyName")
sealed class AbstractMinecartMeta : AbstractVehicleMeta() {
    companion object : AbstractMinecartMeta()

    val CUSTOM_BLOCK_ID_AND_DAMAGE = index(MetadataEntry.Type.OPT_BLOCK_STATE, Block.AIR)
    val CUSTOM_BLOCK_Y_POSITION = index(MetadataEntry.Type.VAR_INT, 6)
    val SHOW_CUSTOM_BLOCK = index(MetadataEntry.Type.BOOLEAN, false)
}