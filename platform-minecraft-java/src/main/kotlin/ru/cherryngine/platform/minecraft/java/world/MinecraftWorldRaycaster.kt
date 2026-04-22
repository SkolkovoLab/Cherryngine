package ru.cherryngine.platform.minecraft.java.world

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.world.ServerWorld
import ru.cherryngine.engine.core.world.RaycastHit
import ru.cherryngine.engine.core.world.WorldRaycaster
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.platform.minecraft.java.MinecraftServerWorld
import kotlin.math.floor

/**
 * Простой stepping-raycaster (шаг 0.1 блока) по `MinecraftServerWorld`.
 * Останавливается на первом не-air блоке, возвращает его ключ как строку
 * `"minecraft:<block>"`.
 */
@Singleton
class MinecraftWorldRaycaster : WorldRaycaster<MinecraftServerWorld> {

    override fun canHandle(target: ServerWorld): Boolean = target is MinecraftServerWorld

    override fun raycast(
        from: Vec3D,
        direction: Vec3D,
        maxDistance: Double,
        world: MinecraftServerWorld,
        contextIDs: Set<String>,
    ): RaycastHit? {
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
            val block = world.getBlock(blockPos, contextIDs)
            if (!block.isAir) {
                return RaycastHit(pos, blockPos, block.key().asString())
            }
            distance += stepSize
        }
        return null
    }
}
