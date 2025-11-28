package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent
import ru.cherryngine.engine.ecs.components.PositionComponent
import ru.cherryngine.engine.ecs.events.LastPlayerPositionEvent

class WriteClientPositionSystem(
    val playerManager: PlayerManager,
) : IteratingSystem(
    family { all(PlayerComponent, PositionComponent, LastPlayerPositionEvent) }
) {
    override fun onTickEntity(entity: EcsEntity) {
        val playerComponent = entity[PlayerComponent]
        val positionComponent = entity[PositionComponent]
        val lastPlayerPositionEvent = entity[LastPlayerPositionEvent]
        val player = playerManager.getPlayerNullable(playerComponent.uuid) ?: return

        if (positionComponent.position != lastPlayerPositionEvent.position || positionComponent.yawPitch != lastPlayerPositionEvent.yawPitch) {
            player.teleport(positionComponent.position, positionComponent.yawPitch)
        }
    }
}