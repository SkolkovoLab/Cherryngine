package ru.cherryngine.engine.ecs

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import ru.cherryngine.engine.core.player.MovementSnapshot
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerPositionShadow
import ru.cherryngine.engine.ecs.components.PositionComponent
import kotlin.time.Duration

/**
 * POST-стадия: если ECS-позиция разошлась с клиентом (что-то двигало
 * игрока на сервере во время тика), телепортируем клиента к ECS.
 * Сохраняем актуальную ECS-позицию в shadow для PreSync.
 */
@InstanceSingleton(stage = TickStage.POST)
class PlayerPositionPostSyncTickable(
    private val playerManager: PlayerManager,
    private val ecsWorld: EcsWorld,
    private val shadow: PlayerPositionShadow,
) : Tickable {
    override fun tick(delta: Duration) {
        for (player in playerManager.onlinePlayers()) {
            val entity = ecsWorld.getPlayerEntityOrNull(player.uuid) ?: continue
            val ecsPos = with(ecsWorld) { entity.getOrNull(PositionComponent) } ?: continue
            if (ecsPos.position != player.clientPosition || ecsPos.yawPitch != player.clientYawPitch) {
                player.teleport(ecsPos.position, ecsPos.yawPitch)
            }
            shadow[player.uuid] = MovementSnapshot(ecsPos.position, ecsPos.yawPitch, false)
        }
    }
}
