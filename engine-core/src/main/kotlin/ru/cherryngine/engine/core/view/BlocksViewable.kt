package ru.cherryngine.engine.core.view

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.world.block.Block

/**
 * В отличие от обычного Viewable, этот подразумевает, что chunkPos иммутабельный
 */
interface BlocksViewable : Viewable {
    fun getBlockId(pos: Vec3I): Int?
    fun getBlock(pos: Vec3I): Block?
}