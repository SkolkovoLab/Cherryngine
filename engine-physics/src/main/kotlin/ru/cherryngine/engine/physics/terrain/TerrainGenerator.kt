package ru.cherryngine.engine.physics.terrain

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.world.TerrainCollisionDispatcher
import ru.cherryngine.engine.physics.PhysicsSpace
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3I
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

/**
 * Генерирует terrain-тела вокруг активных physics-тел. Платформенная специфика
 * (какой блок → какой список AABB) инкапсулирована в [TerrainCollisionDispatcher].
 */
@InstanceSingleton
class TerrainGenerator(
    private val physicsSpace: PhysicsSpace,
    private val terrainCollision: TerrainCollisionDispatcher,
) {
    private data class TerrainKey(val pos: Vec3I, val contextKey: Set<String>)
    private data class TerrainBodyEntry(val body: PhysicsSpace.PhysicsBody, val cuboids: List<Cuboid>)

    private val terrainBodies = HashMap<TerrainKey, TerrainBodyEntry>()

    fun step(delta: Float, activeBodies: List<ActiveBodyInfo>) {
        val keep = HashSet<TerrainKey>()

        for (bodyInfo in activeBodies) {
            val d = bodyInfo.velocity * delta.toDouble() * 0.25
            val aabb = bodyInfo.aabb.expand(0.5).expand(
                max(0.0, -d.x), max(0.0, -d.y), max(0.0, -d.z),
                max(0.0, d.x), max(0.0, d.y), max(0.0, d.z),
            )

            forEachBlockInAABB(aabb) { pos ->
                val cuboids = terrainCollision.getCollisionCuboids(pos, bodyInfo.physContextIDs)
                if (cuboids.isEmpty()) return@forEachBlockInAABB

                val key = TerrainKey(pos, bodyInfo.physContextIDs)
                keep.add(key)
                val existing = terrainBodies[key]
                if (existing != null && existing.cuboids == cuboids) return@forEachBlockInAABB

                existing?.let { physicsSpace.unregisterBodyContexts(it.body); it.body.remove() }
                val body = physicsSpace.addTerrain(pos, cuboids)
                physicsSpace.registerBodyContexts(body, key.contextKey)
                terrainBodies[key] = TerrainBodyEntry(body, cuboids)
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
