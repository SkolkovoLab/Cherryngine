package ru.cherryngine.engine.physics.terrain

import ru.cherryngine.engine.physics.PhysicsSpace
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.LayeredWorld
import kotlin.math.ceil
import kotlin.math.floor

class TerrainGenerator(private val physicsSpace: PhysicsSpace) {
    private data class TerrainKey(val pos: Vec3I, val contextKey: Set<String>)

    private data class TerrainBodyEntry(
        val body: PhysicsSpace.PhysicsBody,
        val blockStateId: Int,
    )

    private val terrainBodies = HashMap<TerrainKey, TerrainBodyEntry>()

    fun step(activeBodies: List<ActiveBodyInfo>, layers: List<LayerWithContext>) {
        val keep = HashSet<TerrainKey>()

        for (bodyInfo in activeBodies) {
            val relevantLayers = layers
                .filter { lc -> lc.contextIDs.any { it in bodyInfo.physContextIDs } }
            if (relevantLayers.isEmpty()) continue

            val dimensionType = relevantLayers.first().dimensionType
            val layeredWorld = LayeredWorld(dimensionType, relevantLayers.map { it.entry })

            val aabb = bodyInfo.aabb.expand(0.5)

            forEachBlockInAABB(aabb) { pos ->
                val block = layeredWorld.getBlock(pos)
                if (block.isAir()) return@forEachBlockInAABB

                val key = TerrainKey(pos, bodyInfo.physContextIDs)
                keep.add(key)
                val stateId = block.getStateId()
                val existing = terrainBodies[key]
                if (existing != null && existing.blockStateId == stateId) return@forEachBlockInAABB

                existing?.body?.remove()
                val body = physicsSpace.addTerrain(pos)
                terrainBodies[key] = TerrainBodyEntry(body, stateId)
            }
        }

        terrainBodies.entries.removeIf { (key, entry) ->
            if (key !in keep) {
                entry.body.remove()
                true
            } else false
        }
    }

    private inline fun forEachBlockInAABB(aabb: Cuboid, action: (Vec3I) -> Unit) {
        val minX = floor(aabb.min.x).toInt()
        val minY = floor(aabb.min.y).toInt()
        val minZ = floor(aabb.min.z).toInt()
        val maxX = ceil(aabb.max.x).toInt()
        val maxY = ceil(aabb.max.y).toInt()
        val maxZ = ceil(aabb.max.z).toInt()

        for (x in minX until maxX) {
            for (y in minY until maxY) {
                for (z in minZ until maxZ) {
                    action(Vec3I(x, y, z))
                }
            }
        }
    }
}

data class ActiveBodyInfo(
    val aabb: Cuboid,
    val physContextIDs: Set<String>,
)

data class LayerWithContext(
    val entry: LayerEntry,
    val contextIDs: Set<String>,
    val dimensionType: DimensionType,
)
