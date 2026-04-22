package ru.cherryngine.engine.core.world

import ru.cherryngine.engine.core.platform.PlatformHandler
import ru.cherryngine.engine.core.world.ServerWorld
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3I

/**
 * Платформенный провайдер коллизий террейна: умеет по координате блока выдать
 * AABB-кубойды коллизии и свойства поверхности (трение/restitution).
 *
 * Реализации объявляются `@Singleton`-ами в платформенных модулях
 * (`MinecraftTerrainCollisionProvider`, `BedrockTerrainCollisionProvider`
 * и т.п.) и резолвятся через [canHandle] из [TerrainCollisionDispatcher].
 */
interface TerrainCollisionProvider<in W : ServerWorld> : PlatformHandler<ServerWorld> {
    /**
     * Кубоиды коллизии блока в локальных координатах `[0..1]^3`.
     * Пустой список = нет коллизии (воздух, jelly и т.п.).
     */
    fun getCollisionCuboids(
        pos: Vec3I,
        world: W,
        contextIDs: Set<String>,
    ): List<Cuboid>

    fun getSurfaceProperties(
        pos: Vec3I,
        world: W,
        contextIDs: Set<String>,
    ): SurfaceProperties = SurfaceProperties()

    data class SurfaceProperties(
        val friction: Float = 1.0f,
        val restitution: Float = 0.0f,
    )
}
