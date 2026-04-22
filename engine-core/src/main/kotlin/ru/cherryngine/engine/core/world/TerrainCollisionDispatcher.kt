package ru.cherryngine.engine.core.world

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.world.ServerWorld
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3I

@InstanceSingleton
class TerrainCollisionDispatcher(
    private val providers: List<TerrainCollisionProvider<*>>,
    private val serverWorld: ServerWorld,
) {
    @Suppress("UNCHECKED_CAST")
    private val provider: TerrainCollisionProvider<ServerWorld> by lazy {
        (providers.firstOrNull { it.canHandle(serverWorld) }
            ?: error("No TerrainCollisionProvider for ${serverWorld::class.simpleName}"))
            as TerrainCollisionProvider<ServerWorld>
    }

    fun getCollisionCuboids(pos: Vec3I, contextIDs: Set<String>): List<Cuboid> =
        provider.getCollisionCuboids(pos, serverWorld, contextIDs)

    fun getSurfaceProperties(pos: Vec3I, contextIDs: Set<String>): TerrainCollisionProvider.SurfaceProperties =
        provider.getSurfaceProperties(pos, serverWorld, contextIDs)
}
