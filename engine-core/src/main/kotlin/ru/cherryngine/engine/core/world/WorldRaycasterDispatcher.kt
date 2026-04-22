package ru.cherryngine.engine.core.world

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.world.ServerWorld
import ru.cherryngine.lib.math.Vec3D

@InstanceSingleton
class WorldRaycasterDispatcher(
    private val raycasters: List<WorldRaycaster<*>>,
    private val serverWorld: ServerWorld,
) {
    @Suppress("UNCHECKED_CAST")
    private val raycaster: WorldRaycaster<ServerWorld> by lazy {
        (raycasters.firstOrNull { it.canHandle(serverWorld) }
            ?: error("No WorldRaycaster for ${serverWorld::class.simpleName}"))
            as WorldRaycaster<ServerWorld>
    }

    fun raycast(from: Vec3D, direction: Vec3D, maxDistance: Double, contextIDs: Set<String>): RaycastHit? =
        raycaster.raycast(from, direction, maxDistance, serverWorld, contextIDs)
}
