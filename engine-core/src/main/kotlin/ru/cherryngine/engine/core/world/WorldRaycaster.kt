package ru.cherryngine.engine.core.world

import ru.cherryngine.engine.core.platform.PlatformHandler
import ru.cherryngine.engine.core.world.ServerWorld
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I

/**
 * Платформенный raycaster по блокам мира. Реализации — `@Singleton`-ы
 * в платформенных модулях, резолвятся через [canHandle] из [WorldRaycasterDispatcher].
 */
interface WorldRaycaster<in W : ServerWorld> : PlatformHandler<ServerWorld> {
    fun raycast(
        from: Vec3D,
        direction: Vec3D,
        maxDistance: Double,
        world: W,
        contextIDs: Set<String>,
    ): RaycastHit?
}

/**
 * Результат raycast'а. `blockMaterial` — кроссплатформенный ID блока
 * вида `"minecraft:stone"`, `"minecraft:ice"` и т.п.
 */
data class RaycastHit(
    val hitPos: Vec3D,
    val blockPos: Vec3I,
    val blockMaterial: String,
)
