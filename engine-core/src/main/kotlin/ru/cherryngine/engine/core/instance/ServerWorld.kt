package ru.cherryngine.engine.core.instance

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.LayeredWorld

class ServerWorld {
    private val layersByContext = HashMap<String, MutableList<LayerEntry>>()
    var dimensionType: DimensionType? = null

    fun registerLayer(contextID: String, entry: LayerEntry) {
        layersByContext.getOrPut(contextID) { mutableListOf() }.add(entry)
    }

    fun getLayersForContexts(contextIDs: Set<String>): List<LayerEntry> =
        contextIDs.flatMap { layersByContext[it] ?: emptyList() }

    fun getLayersByContext(): Map<String, List<LayerEntry>> = layersByContext

    fun getBlock(pos: Vec3I, contextIDs: Set<String>): Block {
        val dt = dimensionType ?: return Block.AIR
        val layers = getLayersForContexts(contextIDs)
        if (layers.isEmpty()) return Block.AIR
        return LayeredWorld(dt, layers).getBlock(pos)
    }

    fun isSolid(pos: Vec3I, contextIDs: Set<String>): Boolean {
        return !getBlock(pos, contextIDs).isAir()
    }

    fun raycast(
        from: Vec3D,
        direction: Vec3D,
        maxDistance: Double,
        contextIDs: Set<String>,
    ): RaycastResult? {
        val dt = dimensionType ?: return null
        val layers = getLayersForContexts(contextIDs)
        if (layers.isEmpty()) return null
        val world = LayeredWorld(dt, layers)

        val stepSize = 0.1
        var distance = 0.0
        val dir = direction.normalize()

        while (distance <= maxDistance) {
            val pos = from + dir * distance
            val blockPos = Vec3I(
                Math.floor(pos.x).toInt(),
                Math.floor(pos.y).toInt(),
                Math.floor(pos.z).toInt(),
            )
            val block = world.getBlock(blockPos)
            if (!block.isAir()) {
                return RaycastResult(pos, blockPos)
            }
            distance += stepSize
        }
        return null
    }
}

data class RaycastResult(
    val hitPos: Vec3D,
    val blockPos: Vec3I,
)
