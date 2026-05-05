package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.platform.PlatformHandler
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

/**
 * Источник позиции/ориентации игрока. Реализации могут возвращать live-данные
 * клиента (платформенный источник) или авторитетную серверную позицию (ECS).
 */
interface MovementSource<in P : Player> : PlatformHandler<Player> {
    fun pollMovement(player: P): MovementSnapshot?
}

data class MovementSnapshot(
    val position: Vec3D,
    val yawPitch: YawPitch,
    val onGround: Boolean,
)
