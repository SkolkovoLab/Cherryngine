package ru.cherryngine.engine.physics.terrain

import net.minestom.server.instance.block.Block
import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.physics.PhysicsSpace
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.world.LayeredWorld
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

@InstanceSingleton
class TerrainGenerator(
    private val physicsSpace: PhysicsSpace
) {
    private data class TerrainKey(val pos: Vec3I, val contextKey: Set<String>)

    private data class TerrainBodyEntry(
        val body: PhysicsSpace.PhysicsBody,
        val blockStateId: Int,
    )

    private val terrainBodies = HashMap<TerrainKey, TerrainBodyEntry>()

    fun step(delta: Float, activeBodies: List<ActiveBodyInfo>, layers: List<LayerWithContext>) {
        val keep = HashSet<TerrainKey>()

        for (bodyInfo in activeBodies) {
            val relevantLayers = layers
                .filter { lc -> lc.contextIDs.any { it in bodyInfo.physContextIDs } }
            if (relevantLayers.isEmpty()) continue

            val dimensionType = relevantLayers.first().dimensionType
            val layeredWorld = LayeredWorld(dimensionType, relevantLayers.map { it.entry })

            val d = bodyInfo.velocity * delta.toDouble() * 0.25
            val aabb = bodyInfo.aabb.expand(0.5).expand(
                max(0.0, -d.x), max(0.0, -d.y), max(0.0, -d.z),
                max(0.0, d.x), max(0.0, d.y), max(0.0, d.z),
            )

            forEachBlockInAABB(aabb) { pos ->
                val block = layeredWorld.getBlock(pos)
                if (block.isAir) return@forEachBlockInAABB

                val collisionCuboids = getCollisionCuboids(block)
                if (collisionCuboids.isEmpty()) return@forEachBlockInAABB

                val key = TerrainKey(pos, bodyInfo.physContextIDs)
                keep.add(key)
                val stateId = block.stateId()
                val existing = terrainBodies[key]
                if (existing != null && existing.blockStateId == stateId) return@forEachBlockInAABB

                existing?.let { physicsSpace.unregisterBodyContexts(it.body); it.body.remove() }
                val body = physicsSpace.addTerrain(pos, collisionCuboids)
                physicsSpace.registerBodyContexts(body, key.contextKey)
                terrainBodies[key] = TerrainBodyEntry(body, stateId)
            }
        }

        terrainBodies.entries.removeIf { (key, entry) ->
            if (key !in keep) {
                physicsSpace.unregisterBodyContexts(entry.body)
                entry.body.remove()
                true
            } else false
        }
    }

    // TODO: восстановить точную форму коллизий. Наш старый RegistryBlock.collisionShape.cuboids
    //  исчез вместе с кастомным реестром; в Minestom форма — Shape, из которого AABB-cuboids
    //  напрямую не достать. Временно: любой солидный блок — полный единичный куб.
    private fun getCollisionCuboids(block: Block): List<Cuboid> {
        val reg = block.registry() ?: return emptyList()
        return if (reg.isSolid) listOf(UNIT_CUBE) else emptyList()
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

    companion object {
        private val UNIT_CUBE = Cuboid(Vec3D(0.0, 0.0, 0.0), Vec3D(1.0, 1.0, 1.0))
    }
}
