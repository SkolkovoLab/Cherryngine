package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import kotlin.time.Duration

@InstanceSingleton(stage = TickStage.POST)
class PlayerPositionPostSyncTickable(
    private val playerManager: PlayerManager,
    private val sources: List<PlayerPositionSource>,
    private val shadow: PlayerPositionShadow,
) : Tickable {
    override fun tick(delta: Duration) {
        for (player in playerManager.onlinePlayers()) {
            val source = sources.firstOrNull { it.canHandle(player) } ?: continue
            val desired = source.getDesired(player) ?: continue
            if (desired.position != player.clientPosition || desired.yawPitch != player.clientYawPitch) {
                player.teleport(desired.position, desired.yawPitch)
            }
            shadow[player.uuid] = desired
        }
    }
}
