package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PositionComponent

class ReadClientPositionSystem(
    val playerManager: PlayerManager,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        val playerComponent = entity[PlayerComponent]
        val player = playerManager.getPlayerNullable(playerComponent.uuid) ?: return

        entity.configure {
            val positionComponent = it.getOrAdd(PositionComponent, ::PositionComponent)
            positionComponent.position = player.clientPosition
            positionComponent.yawPitch = player.clientYawPitch
        }
    }
}