package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import kotlin.time.Duration

@InstanceSingleton(stage = TickStage.PRE)
class PlayerPositionPreSyncTickable(
    private val playerManager: PlayerManager,
    private val sources: List<PlayerPositionSource>,
    private val shadow: PlayerPositionShadow,
) : Tickable {
    override fun tick(delta: Duration) {
        for (player in playerManager.onlinePlayers()) {
            val source = sources.firstOrNull { it.canHandle(player) } ?: continue
            val desired = source.getDesired(player) ?: continue
            val applied = shadow[player.uuid]
            if (applied != null && desired == applied) {
                source.acceptClientMovement(player, player.clientPosition, player.clientYawPitch)
                shadow[player.uuid] = PositionSnapshot(player.clientPosition, player.clientYawPitch)
            }
        }
    }
}
