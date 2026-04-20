package ru.cherryngine.engine.core.instance

import net.minestom.server.instance.block.Block
import net.minestom.server.world.DimensionType
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.LayeredWorld

class ServerWorld {
    private val layersByContext = HashMap<String, MutableList<LayerEntry>>()
    private val worldCache = HashMap<Set<String>, LayeredWorld>()
    var dimensionType: DimensionType? = null
        set(value) {
            field = value
            worldCache.clear()
        }

    fun registerLayer(contextID: String, entry: LayerEntry) {
        layersByContext.getOrPut(contextID) { mutableListOf() }.add(entry)
        worldCache.clear()
    }

    fun getLayersForContexts(contextIDs: Set<String>): List<LayerEntry> =
        contextIDs.flatMap { layersByContext[it] ?: emptyList() }

    fun getLayersByContext(): Map<String, List<LayerEntry>> = layersByContext

    private fun getLayeredWorld(contextIDs: Set<String>): LayeredWorld? {
        val dt = dimensionType ?: return null
        val layers = getLayersForContexts(contextIDs)
        if (layers.isEmpty()) return null
        return worldCache.getOrPut(contextIDs) { LayeredWorld(dt, layers) }
    }

    fun getBlock(pos: Vec3I, contextIDs: Set<String>): Block {
        return getLayeredWorld(contextIDs)?.getBlock(pos) ?: Block.AIR
    }

    fun isSolid(pos: Vec3I, contextIDs: Set<String>): Boolean {
        return !getBlock(pos, contextIDs).isAir
    }

    fun raycast(
        from: Vec3D,
        direction: Vec3D,
        maxDistance: Double,
        contextIDs: Set<String>,
    ): RaycastResult? {
        val world = getLayeredWorld(contextIDs) ?: return null
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
            if (!world.getBlock(blockPos).isAir) {
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
