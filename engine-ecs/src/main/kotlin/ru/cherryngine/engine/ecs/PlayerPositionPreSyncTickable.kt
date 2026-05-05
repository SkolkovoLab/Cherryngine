package ru.cherryngine.engine.ecs

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import ru.cherryngine.engine.core.player.MovementDispatcher
import ru.cherryngine.engine.core.player.MovementSnapshot
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerPositionShadow
import ru.cherryngine.engine.ecs.components.PositionComponent
import kotlin.time.Duration

/**
 * PRE-стадия: если ECS-PositionComponent не двигался с прошлого тика
 * (равен последнему applied), доверяем клиентскому движению — пишем
 * client position в ECS. Сигнал клиента читаем через MovementDispatcher.
 */
@InstanceSingleton(stage = TickStage.PRE)
class PlayerPositionPreSyncTickable(
    private val playerManager: PlayerManager,
    private val ecsWorld: EcsWorld,
    private val movementDispatcher: MovementDispatcher,
    private val shadow: PlayerPositionShadow,
) : Tickable {
    override fun tick(delta: Duration) {
        for (player in playerManager.onlinePlayers()) {
            val entity = ecsWorld.getPlayerEntityOrNull(player.uuid) ?: continue
            val ecsPos = with(ecsWorld) { entity.getOrNull(PositionComponent) } ?: continue
            val desired = MovementSnapshot(ecsPos.position, ecsPos.yawPitch, false)
            val applied = shadow[player.uuid]
            if (applied != null && desired == applied) {
                val client = movementDispatcher.pollMovement(player) ?: continue
                val normalized = MovementSnapshot(client.position, client.yawPitch, false)
                ecsPos.position = normalized.position
                ecsPos.yawPitch = normalized.yawPitch
                shadow[player.uuid] = normalized
            }
        }
    }
}
