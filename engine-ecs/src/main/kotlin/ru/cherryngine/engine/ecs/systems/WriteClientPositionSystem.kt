package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent
import ru.cherryngine.engine.ecs.components.PositionComponent

class WriteClientPositionSystem(
    val playerManager: PlayerManager,
) : IteratingSystem(
    family { all(PlayerComponent, PositionComponent) }
) {
    override fun onTickEntity(entity: EcsEntity) {
        val playerComponent = entity[PlayerComponent]
        val positionComponent = entity[PositionComponent]
        val player = playerManager.getPlayerNullable(playerComponent.uuid) ?: return

        if (positionComponent.position != player.clientPosition || positionComponent.yawPitch != player.clientYawPitch) {
            player.teleport(positionComponent.position, positionComponent.yawPitch)
        }
    }
}