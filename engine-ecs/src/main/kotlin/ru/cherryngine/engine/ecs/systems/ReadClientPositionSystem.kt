package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent
import ru.cherryngine.engine.ecs.components.PositionComponent
import ru.cherryngine.engine.ecs.events.LastPlayerPositionEvent

class ReadClientPositionSystem(
    val playerManager: PlayerManager,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    override fun onTickEntity(entity: EcsEntity) {
        val playerComponent = entity[PlayerComponent]
        val player = playerManager.getPlayerNullable(playerComponent.uuid) ?: return

        entity.configure {
            val positionComponent = it.getOrAdd(PositionComponent, ::PositionComponent)
            val clientPosition = player.clientPosition
            val clientYawPitch = player.clientYawPitch
            positionComponent.position = clientPosition
            positionComponent.yawPitch = clientYawPitch
            it += LastPlayerPositionEvent(clientPosition, clientYawPitch)
        }
    }
}