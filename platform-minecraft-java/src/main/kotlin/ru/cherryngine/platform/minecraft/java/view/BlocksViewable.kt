package ru.cherryngine.platform.minecraft.java.view

import net.minestom.server.instance.block.Block
import ru.cherryngine.lib.math.Vec3I

/**
 * В отличие от обычного Viewable, этот подразумевает, что chunkPos иммутабельный
 */
interface BlocksViewable : Viewable {
    fun getBlockId(pos: Vec3I): Int?
    fun getBlock(pos: Vec3I): Block?
}
