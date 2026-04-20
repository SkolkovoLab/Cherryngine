package ru.cherryngine.engine.minecraft.world

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.instance.ServerWorld
import ru.cherryngine.engine.core.world.RaycastHit
import ru.cherryngine.engine.core.world.WorldRaycaster
import ru.cherryngine.engine.minecraft.MinecraftServerWorld
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import kotlin.math.floor

/**
 * Простой stepping-raycaster (шаг 0.1 блока) по `MinecraftServerWorld`.
 * Останавливается на первом не-air блоке, возвращает его ключ как строку
 * `"minecraft:<block>"`.
 */
@Singleton
class MinecraftWorldRaycaster : WorldRaycaster {

    override fun canHandle(world: ServerWorld): Boolean = world is MinecraftServerWorld

    override fun raycast(
        from: Vec3D,
        direction: Vec3D,
        maxDistance: Double,
        world: ServerWorld,
        contextIDs: Set<String>,
    ): RaycastHit? {
        val mcWorld = world as MinecraftServerWorld
        val stepSize = 0.1
        var distance = 0.0
        val dir = direction.normalize()
        while (distance <= maxDistance) {
            val pos = from + dir * distance
            val blockPos = Vec3I(
                floor(pos.x).toInt(),
                floor(pos.y).toInt(),
                floor(pos.z).toInt(),
            )
            val block = mcWorld.getBlock(blockPos, contextIDs)
            if (!block.isAir) {
                return RaycastHit(pos, blockPos, block.key().asString())
            }
            distance += stepSize
        }
        return null
    }
}
